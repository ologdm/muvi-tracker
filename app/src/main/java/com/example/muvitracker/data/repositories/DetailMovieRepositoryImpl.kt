package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.MovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.dto.movie.detail.mergeMoviesDtoToEntity
import com.example.muvitracker.data.dto.movie.toDomain
import com.example.muvitracker.data.dto.person.toDomain
import com.example.muvitracker.data.utils.mapToIoResponse
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.domain.model.base.MovieBase
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.map
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    private val detailTraktDao = database.movieDao()
    private val prefsMoviesDao = database.prefsMovieDao()

    /**
     * Nel Fetcher, il tipo di ritorno di `DetailMovieTmdbDto` è nullable.
     * Il Fetcher deve lanciare un'eccezione solo se l'intero processo di fetch fallisce.
     * Per evitare che un errore nel recupero da TMDB causi un `FetcherResult.Error.Exception(ex)` sullo Store,
     * utilizziamo un blocco try-catch e restituiamo `null` in caso di eccezione gestita.
     *
     * Nota: bisogna gestire correttamente anche le eccezioni di tipo `CancellationException`,
     */
    private val store = storeFactory<Ids, MovieEntity, Movie>(
        fetcher = { movieIds ->
            coroutineScope {
                // 1° chiamata async
                val traktDtoDeferred = async { traktApi.getMovieDetail(movieIds.trakt) }
                // 2° chiamata async
                val tmdbDtoDeferred = async {
                    try { // try/catch necessario per evitare che l'eccezione mi blocchi la coroutine padre!!
                        tmdbApi.getMovieDto(movieIds.tmdb)
                    } catch (ex: CancellationException) {
                        throw ex // deve propagare le cancellation
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null // fallback: ritorna null se qualsiasi altra eccezione
                    }
                }
                val traktDto = traktDtoDeferred.await()
                val tmdbDto = tmdbDtoDeferred.await()

                mergeMoviesDtoToEntity(traktDto, tmdbDto) // ritorna entity
            }
        },
        reader = { movieIds ->
            combineWithPrefsAndMapToDomainAsFlow(movieIds.trakt)
        },
        writer = { _, movieEntity ->
            detailTraktDao.insertSingle(movieEntity)
        }
    )


    private fun combineWithPrefsAndMapToDomainAsFlow(movieId: Int): Flow<Movie?> {
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

    override fun getSingleDetailMovieFlow(movieIds: Ids): Flow<IoResponse<Movie>> {
        return store.stream(StoreRequest.cached(key = movieIds, refresh = true))
            .mapToIoResponse()
    }


    // for prefs view
    override fun getDetailListFlow(): Flow<List<MovieEntity>> {
        return detailTraktDao.readAllFlow()
    }


    // RELATED MOVIES ------------------------------------------------------------------------------
    override suspend fun getRelatedMovies(movieId: Int): IoResponse<List<MovieBase>> {
        return try {
            // map: movie base -> movie base dto
            IoResponse.Success(traktApi.getMovieRelatedMovies(movieId))
                .map { dtos ->
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

    // CAST ---------------------------------------------------------------
    override suspend fun getMovieCast(movieId: Int): IoResponse<CastAndCrew> {
        return try {
            IoResponse.Success(traktApi.getAllMovieCast(movieId).toDomain())
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Throwable) {
            ex.printStackTrace()
            IoResponse.Error(ex)
        }
    }

}
