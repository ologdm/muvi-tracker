package com.example.muvitracker.data

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.show.DetailShowDto
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.data.dto.show.toEntity
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.domain.model.base.Show
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// solo per detail fragment
// 1. detail base
// 2. season
// 3. relatedShows


@Singleton
class DetailShowRepository @Inject constructor(
    private val traktApi: TraktApi,
    database: MyDatabase,
) {

    private val detailShowDao = database.detailShowDao()
    private val prefsShowDao = database.prefsShowDao()
    private val episodeDao = database.episodesDao()


    // DETAIL
    // store
    private val detailStore: Store<Int, DetailShowEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { showId ->
            try {
                FetcherResult.Data(traktApi.getShowDetail(showId))

            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, DetailShowDto, DetailShowEntity>(
            reader = { showId ->
                detailShowDao.readSingleFlow(showId)
            },
            writer = { _, dto ->
                try {
                    detailShowDao.insertSingle(dto.toEntity())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        )
    ).build()



    fun getSingleDetailShowFlow(showId: Int): Flow<IoResponse<DetailShow>> {
        // flow1
        val detailFlow = detailStore.stream(StoreRequest.cached(key = showId, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }
        // flow2
        val prefsListFLow = prefsShowDao.readAll()
        // flow3
        val watchedStatesFlow: Flow<WatchedDataModel> = getShowWatchedStates(showId)

        // combine flows T1,T2,T3 -> R
        return combine(
            detailFlow, prefsListFLow, watchedStatesFlow
        ) { storeResponse, prefList, watchedStates ->
            when (storeResponse) {
                is StoreResponse.Data -> {
                    val prefsEntity = prefList.find { entity ->
                        entity?.traktId == storeResponse.value.ids.trakt
                    }
                    val detailShow = storeResponse.value
                        .toDomain(prefsEntity)
                        .copy(
                            watchedAll = watchedStates.watchedAll,
                            watchedCount = watchedStates.watchedCount
                        )
                    IoResponse.Success(detailShow)
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


    // RELATED SHOWS
    suspend fun getRelatedShows(showId: Int): IoResponse<List<Show>> {
        return try {
            IoResponse.Success(traktApi.getShowRelatedShows(showId)).ioMapper { dtos ->
                dtos.map { dto ->
                    println("YYY related dto:$dto")
                    dto.toDomain()

                }
            }
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Throwable) {
            ex.printStackTrace()
            println("YYY related error type:$ex")
            IoResponse.Error(ex)
        }
    }

    // CAST - repo separata todo


    // READ WATCHED STATUS - fare con join
    // per checkbox & progress bar insieme OK
    private fun getShowWatchedStates(showId: Int): Flow<WatchedDataModel> {
        return flow {
            // totali show (val fisso) OK
            val airedEpisodes = detailShowDao.readSingleFlow(showId).firstOrNull()?.airedEpisodes
                ?: 0 // suspend function

            episodeDao.countShowWatchedEpisodes(showId)
                .collect { watchedEpisodes ->
                    val watchedAll = (watchedEpisodes == airedEpisodes)
                    emit(WatchedDataModel(watchedAll, watchedEpisodes)) // emit true se coincidono
                }
        }
    }


    // WATCHED ALL SU SHOW TODO !!!!!!!!!!!!!!!
//     //5 coroutines al massimo aperte in una volta
//    suspend fun checkAndToggleShowAllWatchedEpisodes(showId: Int) = coroutineScope {
//
//        val semaphore = Semaphore(5)
//        // usare mutex, versione piu avanzata mutex
//
//        val showSeasons = seasonDao.readAllSeasonsOfShow(showId).first()
//
//        val jobs = showSeasons.map { season ->
//            async(Dispatchers.IO) {
//                semaphore.withPermit {
//                    seasonRepo.checkAndToggleWatchedAllSeasonEpisodes(showId, season.seasonNumber)
//                }
//            }
//        }
//        jobs.awaitAll()
//    }


    //     // TOGGLE WATCHED  - old test, sequenziale
//     // per click on checkbox
//    suspend fun checkAndToggleShowAllWatchedEpisodes(showId: Int) {
//        // itera stagione per stagione e scarica tutti i dati necessari
//        val showSeasons = seasonDao.readAllSeasonsOfShow(showId).collect{
//            for (season in it) {
//                seasonRepo.checkAndToggleWatchedAllSeasonEpisodes(showId, season.seasonNumber)
//            }
//        }
//    }


    // TODO
    // LOGICA AGGIUNTA PREFS QUANDO UN EPISODE IS WATCHED

}


