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


    private val detailStore = store<Int, DetailShowDto, DetailShow>(
        fetcher = { showId ->
            traktApi.getShowDetail(showId)
        },
        reader = {showId ->
            detailShowDao.getSingleFlow(showId)
        } ,
        writer = {_,dto   ->
            detailShowDao.insertSingle(dto.toEntity())
        }
    )



    fun getSingleDetailShowFlow(showId: Int): Flow<IoResponse<DetailShow?>> {
        return detailStore.stream(StoreRequest.cached(key = showId, refresh = true))
            .convertIoResponse()
    }


    // RELATED SHOWS
    suspend fun getRelatedShows(showId: Int): IoResponse<List<Show>> {
        return try {
            IoResponse.Success(traktApi.getShowRelatedShows(showId)).ioMapper { dtos ->
                dtos.map { dto ->
                    dto.toDomain()
                }
            }
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Throwable) {
            ex.printStackTrace()
            IoResponse.Error(ex)
        }
    }


    // WATCHED ALL SU SHOW
    // 10 coroutines al massimo aperte in una volta
    suspend fun checkAndSetShowWatchedAllSeasons(showId: Int) = coroutineScope {
        val showAllSeasonsCount = seasonDao.countAllSeasonsOfShow(showId)
        val isShowWatchedAll = detailShowDao.getSingleFlow(showId)
                .firstOrNull()?.watchedAll // watchedAll calculated
                ?: return@coroutineScope

        val semaphore = Semaphore(20)
        val jobs = (1..showAllSeasonsCount).map { season ->
            async {
                semaphore.withPermit {
                    if (isShowWatchedAll) {
                        seasonRepo.checkAndSetSingleSeasonWatchedAllEpisodes(showId, season, false)
                    } else {
                        seasonRepo.checkAndSetSingleSeasonWatchedAllEpisodes(showId, season, true)
                    }
                }
            }
        }
        jobs.awaitAll()
    }

}




