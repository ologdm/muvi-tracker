package com.example.muvitracker.data

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.fresh
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.database.entities.copyDtoData
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.season.toEntity
import com.example.muvitracker.data.requests.ShowRequestKeys
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// contiene lista episodi totale

@Singleton
class SeasonRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase,
    private val episodeRepository: EpisodeRepository
) {
    private val seasonDao = database.seasonsDao()
    private val episodeDao = database.episodesDao()


    // SEASON from detail

    // 1. store OK
    private val seasonStore: Store<Int, List<SeasonExtended>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { showId ->
            try {
                FetcherResult.Data(traktApi.getAllSeasons(showId)
                    .filter { it.number != 0 })// exclude season 0, specials
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, List<SeasonExtenDto>, List<SeasonExtended>>(
            reader = { showId ->
//                seasonDao.readAllSeasonsOfShow(showId) // old
                seasonDao.getAllSeasonsExtended(showId) //
            },
            writer = { showId, dtos ->
                saveSeasonDtosToDatabase(showId, dtos)
            }
        )
    ).build()


    // 0000
    suspend fun saveSeasonDtosToDatabase(showId: Int, dtos: List<SeasonExtenDto>) {
        // if (non esiste) insertNuovo, else updateParziale
        for (seasonDto in dtos) {
            val dtoIndex = seasonDto.ids.trakt
            val entity = seasonDao.readSingleSeasonById(dtoIndex)
            if (entity == null) {
                seasonDao.insertSingle(seasonDto.toEntity(showId))
            } else {
                val updatedEntity = entity.copyDtoData(seasonDto)
                seasonDao.updateSingleSeasonDto(updatedEntity)
            }
        }
    }


    // 3. stream
    // con SeasonExtended
    fun getAllSeasonsFlow(showId: Int): Flow<IoResponse<List<SeasonExtended>>> {

        return seasonStore.stream(StoreRequest.cached(showId, refresh = true))
            .filterNot { storeResponse ->
                storeResponse is StoreResponse.Loading || storeResponse is StoreResponse.NoNewData
            }
            .map { storeResponse ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        IoResponse.Success(storeResponse.value)
                    }

                    is StoreResponse.Error.Exception -> {
                        IoResponse.Error(storeResponse.error)
                    }

                    is StoreResponse.Error.Message -> {
                        IoResponse.Error(RuntimeException(storeResponse.message))
                    }

                    is StoreResponse.Loading,
                    is StoreResponse.NoNewData -> error("should be filtered upstream")

                }
            }
    }


    // 1. solo lettura da db elemento singolo (elemento esiste sicuramente)
//    suspend fun getSingleSeason(showId: Int, seasonNr: Int): SeasonEntity? {
//        return seasonDao.readSingleSeason(showId, seasonNr)
//    }
    fun getSingleSeasonFlow(showId: Int, seasonNr: Int): Flow<SeasonExtended> {
        // !!deve essere flow perchè stato watchedAll puo sempre cambiare
        return seasonDao.getSingleSeasonExtended(showId, seasonNr)
    }


    // #############################################################################################
    // Force Season Watched Click (noData vs Data) 0000
    private suspend fun areEpisodesAvailableForSeason(
        showId: Int,
        seasonNr: Int
    ): Boolean {
        return episodeDao.countEpisodesBySeason(showId, seasonNr) > 0

    }


    // funzione unica per detail & season fragment 0000
    suspend fun checkAndToggleWatchedAllSeasonEpisodes(
        showId: Int,
        seasonNr: Int,
    ) {
        if (!areEpisodesAvailableForSeason(showId, seasonNr)) {
            // 1. scarica tutti gli episodi stagione
            episodeRepository
                .episodeStore
                .fresh(ShowRequestKeys(showId = showId, seasonNr = seasonNr))
        }

        // 2 toggle tutti episodi season
        val seasonAiredEpisodes =
            seasonDao.readSingleSeason(showId, seasonNr)?.airedEpisodes ?: 0
        val seasonWatchedAll =
            episodeDao.countSeasonWatchedEpisodes(showId, seasonNr).first()
        if (seasonWatchedAll == seasonAiredEpisodes) {
            episodeDao.toggleSeasonAllWatchedAEpisodes(showId, seasonNr, false)
        } else {
            episodeDao.toggleSeasonAllWatchedAEpisodes(showId, seasonNr, true)
        }

        // todo egde case
        // - se puntate aumentano, devo watchedAll scompare, airedEpisodes 10->13, 3 episodi non scaricati
    }

}

//    fun getShowSeasonsFlowOld(showId: Int): Flow<IoResponse<List<SeasonEntity>>> {
//        return seasonStore.stream(StoreRequest.cached(showId, refresh = true))
//            .filterNot { storeResponse ->
//                storeResponse is StoreResponse.Loading || storeResponse is StoreResponse.NoNewData
//            }
//            .map { storeResponse ->
//                when (storeResponse) {
//                    is StoreResponse.Data -> {
//                        IoResponse.Success(storeResponse.value)// no map toDomain
//                    }
//
//                    is StoreResponse.Error.Exception -> {
//                        IoResponse.Error(storeResponse.error)
//                    }
//
//                    is StoreResponse.Error.Message -> {
//                        IoResponse.Error(RuntimeException(storeResponse.message))
//                    }
//
//                    is StoreResponse.Loading,
//                    is StoreResponse.NoNewData -> error("should be filtered upstream")
//                }
//            }
//    }


// todo - singolo -> join su season dao
//                private fun getSeasonWatchedStates(
//                    showId: Int,
//                    seasonNr: Int
//                ): Flow<WatchedDataModel> {
//                    return flow {
//                        // totali stagione (val fisso)
//                        val airedEpisodes =
//                            seasonDao.readSingleSeason(showId, seasonNr)?.airedEpisodes
//                                ?: 0 // suspend function
//
//                        // watched (flow)
//                        episodeDao.countSeasonWatchedEpisodes(showId, seasonNr)
//                            .collect { watchedEpisodes ->
//                                val watchedAll = (watchedEpisodes == airedEpisodes)
//                                emit(WatchedDataModel(watchedAll, watchedEpisodes))
//                            }
//                    }
//                }


//private fun getAllSeasonsWatchedStates(showId: Int): Flow<List<WatchedDataModel>> {
//        return flow {
//            val watchedDataList = mutableListOf<WatchedDataModel>()
//
//            // Ottenere tutte le stagioni per lo show
//            val airedSeasons = seasonDao.readAllSeasonsOfShow(showId).first()
//            for (season in airedSeasons) {
//                val airedEpisodes = season.airedEpisodes
//
//                val watchedEpisodes =
//                    episodeDao.countSeasonWatchedEpisodes(showId, season.seasonNumber).first()
//
//                // watched (flow)
//                val watchedAll = (watchedEpisodes == airedEpisodes)
//                watchedDataList.add(
//                    WatchedDataModel(
//                        showId = showId,
//                        seasonNumber = season.seasonNumber,
//                        watchedAll = watchedAll,
//                        watchedCount = watchedEpisodes
//                    )
//                )
//            }
//            emit(watchedDataList)
//
//        }
//
//    }




//    fun checkSeasonAllWatchedStatus(showId: Int, seasonNr: Int): Flow<Boolean> {
//        return flow {
//            // totali stagione (val fisso)
//            val airedEpisodes =
//                seasonDao.readSingleSeason(showId, seasonNr)?.airedEpisodes ?: 0 // suspend function
//
//            // watched (flow)
//            episodeDao.countSeasonWatchedEpisodes(showId, seasonNr)
//                .collect { watchedEpisodes ->
//                    emit(watchedEpisodes == airedEpisodes) // emit true se coincidono
//                }
//        }
//    }

//    // WATCHED
//    // 1. update season
//    suspend fun updateSeasonWatchedCountAndAll(showId: Int, seasonNr: Int) {
//        val watchedEpisodes =
//            episodeDao.checkWatchedEpisodesOfSeason(showId, seasonNr).firstOrNull()?.size
//        episodeDao.countEpisodesBySeason()
//        val seasonTotalEpisodes = seasonDao.readSingleSeason(showId, seasonNr)?.episodeCount
//        // episodeCount(usciti fino ad ora) - dato da server
//        // airedEpisodes (tutti pianificati)
//
//        // todo - eliminare e non aggionare stato su seaon
//        if (watchedEpisodes == seasonTotalEpisodes) {
//            // count=total  true
//            seasonDao.updateWatchedCountOfSingleSeason(
//                showId,
//                seasonNr,
//                watchedAll = true,
//                watchedCount = watchedEpisodes ?: 0
//            )
//        } else {
//            // count<total  false
//            seasonDao.updateWatchedCountOfSingleSeason(
//                showId,
//                seasonNr,
//                watchedAll = false,
//                watchedCount = watchedEpisodes ?: 0
//            )
//        }
//    }



