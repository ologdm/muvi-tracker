package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.dto.DetailShowDto
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.data.utils.mapToIoResponse
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.domain.model.base.Show
import com.example.muvitracker.domain.repo.DetailShowRepository
import com.example.muvitracker.domain.repo.PrefsShowRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Singleton
class DetailShowRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
    private val prefsShowRepository: PrefsShowRepo,
    private val seasonRepo: SeasonRepository,
    database: MyDatabase,
) : DetailShowRepository {
    private val detailShowDao = database.detailShowDao()
    private val seasonDao = database.seasonsDao()

    private val store = storeFactory<Int, DetailShowDto, DetailShow>(
        fetcher = { showId ->
            traktApi.getShowDetail(showId)
        },
        reader = { showId ->
            detailShowDao.getSingleFlow(showId)
        },
        writer = { _, dto ->
            detailShowDao.insertSingle(dto.toEntity())
        }
    )


    override fun getSingleDetailShowFlow(showId: Int): Flow<IoResponse<DetailShow>> {
        return store.stream(StoreRequest.cached(key = showId, refresh = true))
            .mapToIoResponse()
    }


    // RELATED SHOWS
    override suspend fun getRelatedShows(showId: Int): IoResponse<List<Show>> {
        return try {
            IoResponse.Success(traktApi.getShowRelatedShows(showId)).ioMapper { dtos ->
                dtos.map { dto -> dto.toDomain() }
            }
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Throwable) {
            ex.printStackTrace()
            IoResponse.Error(ex)
        }
    }

    override suspend fun toggleLikedShow(showId: Int) {
        prefsShowRepository.toggleLikedOnDB(showId)
    }


    // WATCHED_ALL SU SHOW
    override suspend fun checkAndSetWatchedAllShowEpisodes(showId: Int) = coroutineScope {
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




