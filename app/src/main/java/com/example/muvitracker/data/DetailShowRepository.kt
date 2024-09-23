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
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.movies.MovieBaseDto
import com.example.muvitracker.data.dto.show.DetailShowDto
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.movies.toEntity
import com.example.muvitracker.data.dto.season.toEntity
import com.example.muvitracker.data.dto.show.ShowBaseDto
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.data.dto.show.toEntity
import com.example.muvitracker.data.requests.ShowRequestKeys
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.domain.model.base.Show
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// solo per detail fragment
// 1. detail base
// 2. season


@Singleton
class DetailShowRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase,
    private val seasonRepo : SeasonRepository // todo
) {

    private val detailShowDao = database.detailShowDao()
    private val prefsShowDao = database.prefsShowDao()
    private val seasonDao = database.seasonsDao()
    private val episodeDao = database.episodesDao()


    // DETAIL 00
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


    fun getSingleDetailShowFlow(id: Int): Flow<IoResponse<DetailShow>> {
        // flow1
        val detailFlow = detailStore.stream(StoreRequest.cached(key = id, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }
        // flow2
        val prefsListFLow = prefsShowDao.readAll()

        // combine flows T1, T2 -> R
        return detailFlow
            .combine(prefsListFLow) { storeResponse, prefList ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        val prefsEntity = prefList.find { entity ->
                            entity?.traktId == storeResponse.value.ids.trakt
                        }
                        val detailMovie = storeResponse.value.toDomain(prefsEntity)
                        IoResponse.Success(detailMovie)
                    }

                    is StoreResponse.Error.Exception ->
                        IoResponse.Error(storeResponse.error)

                    is StoreResponse.Error.Message ->
                        IoResponse.Error(RuntimeException(storeResponse.message))

                    is StoreResponse.Loading,
                    is StoreResponse.NoNewData -> error("should be filtered upstream")
                }
            }
    }


    fun getDetailListFlow(): Flow<List<DetailShowEntity?>> {
        return detailShowDao.readAllFlow()
    }



    // RELATED SHOWS todo OK
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


//    private suspend fun areEpisodesAvailableForSeason(showId: Int, seasonNr: Int): Boolean {
        // TODO
//        return episodeDao.countEpisodesBySeason(showId, seasonNr) > 0
//    }

//    suspend fun checkAndToggleWatchedAllSeasonEpisodes(
//        showId: Int,
//        seasonNr: Int,
//    ) {
//        if (!areEpisodesAvailableForSeason(showId, seasonNr)) {
//
//            // ciclo for per ogni stagione
//            // scarica tutti gli episodi stagione
//            episodeRepository.episodeStore.fresh(ShowRequestKeys(showId = showId, seasonNr = seasonNr))
//            // get() non andava perche dao restituiva emptyList, con null funzica
//        }
//
//        // toggle tutti gli episodi stagione
//        episodeRepository.toggleSeasonAllWatchedEpisodes(showId, seasonNr) // episodes
//
//
//        // TODO edge case (stagione in corso) eugi
//        //  - se puntate aumentano, devo watchedAll scompare, airedEpisodes 10->13, 3 episodi non scaricati
//    }


}
