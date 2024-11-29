package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.dto.DetailShowDto
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.domain.model.base.Show
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
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
    private val seasonRepo: SeasonRepository,
    database: MyDatabase,
) {
    private val detailShowDao = database.detailShowDao()
    private val seasonDao = database.seasonsDao()


    // store
    private val detailStore: Store<Int, DetailShow> = StoreBuilder.from(
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
        sourceOfTruth = SourceOfTruth.of<Int, DetailShowDto, DetailShow>(
            reader = { showId ->
                detailShowDao.getSingleDetailFlow(showId)
                // return->DetailShow,
                // join detail + prefs + episodeCount
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


    fun getSingleDetailShowFlow(showId: Int): Flow<IoResponse<DetailShow?>> {
        return detailStore.stream(StoreRequest.cached(key = showId, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }
            .map { storeResponse ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        val detailShow = storeResponse.value
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


    // WATCHED ALL SU SHOW
    // 10 coroutines al massimo aperte in una volta

    suspend fun checkAndSetShowAllWatchedEpisodes(showId: Int) = coroutineScope {
        val showSeasons = seasonDao.countAllSeasonsOfShow(showId)
        val isDetailWatchedStatus =
            detailShowDao.getSingleDetailFlow(showId).firstOrNull()?.watchedAll
                ?: return@coroutineScope

        val semaphore = Semaphore(10)

        val jobs = (1..showSeasons).map { season ->
            async {
                semaphore.withPermit {
                    println("XXX async")
//                    delay(5000) for test
                    if (isDetailWatchedStatus) {
                        seasonRepo.checkAndSetWatchedAllSeasonEpisodes(showId, season, false)
                    } else {
                        seasonRepo.checkAndSetWatchedAllSeasonEpisodes(showId, season, true)
                    }
                }
            }
        }
        jobs.awaitAll()
    }


    // CAST - repo separata todo

}




