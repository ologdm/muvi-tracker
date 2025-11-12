package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.ShowEntity
import com.example.muvitracker.data.dto.show.detail.mergeShowsDtoToEntity
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.dto.person.toDomain
import com.example.muvitracker.data.dto.show.detail.ShowTmdbDto
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.data.utils.mapToIoResponse
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.domain.model.base.ShowBase
import com.example.muvitracker.domain.repo.DetailShowRepository
import com.example.muvitracker.domain.repo.PrefsShowRepository
import com.example.muvitracker.domain.repo.SeasonRepository
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
    private val tmdbApi: TmdbApi,
    private val seasonRepository: SeasonRepository,
    database: MyDatabase,
) : DetailShowRepository {

    private val detailShowDao = database.showDao()
    private val seasonDao = database.seasonsDao()


    private val store = storeFactory<Ids, ShowEntity, Show>(
        fetcher = { ids ->
            coroutineScope {
                // 1° chiamata async
                val traktDtoDeferred = async { traktApi . getShowDetail (ids.trakt)}
                // 2° chiamata async
                val tmdbDtoDeferred = async {
                    try {  // try/catch necessario per evitare che l'eccezione mi blocchi la coroutine padre!!
                        tmdbApi.getShowDto(ids.tmdb)
                    } catch (ex: CancellationException) {
                        throw ex  // deve propagare le cancellation
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null  // fallback: ritorna null se qualsiasi altra eccezione
                    }
                }

                val traktDto = traktDtoDeferred.await()
                val tmdbDto =  tmdbDtoDeferred.await()

                mergeShowsDtoToEntity(traktDto, tmdbDto)
            }
        },
        reader = { ids ->
            // join showEntity + prefsEntity + watchedEpisodes  - on query
            detailShowDao.getSingleFlow(ids.trakt)
        },
        writer = { _, entity ->
            detailShowDao.insertSingle(entity)
        }
    )


    override fun getSingleDetailShowFlow(showIds: Ids): Flow<IoResponse<Show>> {
        return store.stream(StoreRequest.cached(key = showIds, refresh = true))
            .mapToIoResponse()
    }


    // RELATED SHOWS
    override suspend fun getRelatedShows(showId: Int): IoResponse<List<ShowBase>> {
        return try {
            IoResponse.Success(traktApi.getShowRelatedShows(showId))
                .ioMapper { dtos ->
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

    override suspend fun getShowCast(showId: Int): IoResponse<CastAndCrew> {
        return try {
            IoResponse.Success(traktApi.getAllShowCast(showId).toDomain())
        } catch (ex: CancellationException){
            throw ex
        } catch (ex: Throwable){
            ex.printStackTrace()
            IoResponse.Error(ex)
        }
    }


    // WATCHED_ALL SU SHOW
    override suspend fun checkAndSetWatchedAllShowEpisodes(showIds: Ids) = coroutineScope {
        val showAllSeasonsCount = seasonDao.countAllSeasonsOfShow(showIds.trakt)
        val isShowWatchedAll = detailShowDao.getSingleFlow(showIds.trakt)
            .firstOrNull()?.watchedAll // watchedAll calculated
            ?: return@coroutineScope

        val semaphore = Semaphore(20)
        val jobs = (1..showAllSeasonsCount).map { season ->
            async {
                semaphore.withPermit {
                    if (isShowWatchedAll) {
                        seasonRepository.checkAndSetSingleSeasonWatchedAllEpisodes(
                            showIds,
                            season,
                            false
                        )
                    } else {
                        seasonRepository.checkAndSetSingleSeasonWatchedAllEpisodes(
                            showIds,
                            season,
                            true
                        )
                    }
                }
            }
        }
        jobs.awaitAll()
    }


}




