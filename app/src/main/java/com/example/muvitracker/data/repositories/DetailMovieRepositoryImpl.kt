package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.movie.detail.mergeMoviesDtoToEntity
import com.example.muvitracker.data.dto.movie.toDomain
import com.example.muvitracker.data.utils.mapToIoResponse
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


// TODO:
// combinare i 2 flow trakt+tmdb in uno solo

@Singleton
class DetailMovieRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
    private val tmdbApi: TmdbApi,
    database: MyDatabase
) : DetailMovieRepository {

    private val detailTraktDao = database.detailMovieDao()
    private val prefsMoviesDao = database.prefsMovieDao()

    // old
//    private val store = storeFactory<Int, DetailMovieDto, DetailMovie>(
//        fetcher = { movieId ->
//            traktApi.getMovieDetail(movieId)
//        },
//        reader = { movieId ->
//            combineWithPrefsAndMapToDomainAsFlow(movieId)
//        },
//        writer = { _, movieDto ->
//            detailTraktDao.insertSingle(movieDto.toEntity())
//        }
//    )

    // TODO new - input entity, output domain
    private val store = storeFactory<Int, DetailMovieEntity, DetailMovie>(
        fetcher = { movieId ->
            val traktDto = traktApi.getMovieDetail(movieId)
            val tmdbDto= tmdbApi.getMovieDto(traktDto.ids.tmdb)
            mergeMoviesDtoToEntity(traktDto,tmdbDto)
        },
        reader = { movieId ->
            combineWithPrefsAndMapToDomainAsFlow(movieId)
        },
        writer = { _, movieEntity ->
            detailTraktDao.insertSingle(movieEntity)
        }
    )


    // TODO: unire flow di trakt, tmdb, e prefs
    private fun combineWithPrefsAndMapToDomainAsFlow(movieId: Int): Flow<DetailMovie?> {
        val prefsListFlow = prefsMoviesDao.readAll()
        return detailTraktDao.readSingleFlow(movieId)
            .combine(prefsListFlow) { movieEntity, prefsList ->
                val prefsEntity = prefsList.find { entity ->
                    entity.traktId == movieId
                }
                movieEntity?.toDomain(prefsEntity)
            }
    }


    // CONTRACT METHODS ###########################################################

    override fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse<DetailMovie>> {
        return store.stream(StoreRequest.cached(key = id, refresh = true))
            .mapToIoResponse()
    }


    // for prefs view
    override fun getDetailListFlow(): Flow<List<DetailMovieEntity>> {
        return detailTraktDao.readAllFlow()
    }


    // RELATED MOVIES
    override suspend fun getRelatedMovies(movieId: Int): IoResponse<List<Movie>> {
        return try {
            IoResponse.Success(traktApi.getMovieRelatedMovies(movieId))
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


}
