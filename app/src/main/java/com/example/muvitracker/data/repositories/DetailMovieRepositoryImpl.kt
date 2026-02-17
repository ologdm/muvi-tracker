package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.example.muvitracker.MyApp
import com.example.muvitracker.R
import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.MovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.dto.movie.detail.mergeMoviesDtoToEntity
import com.example.muvitracker.data.dto.movie.toDomain
import com.example.muvitracker.data.dto.person.toDomain
import com.example.muvitracker.data.dto.provider.MovieProvidersResponseDto
import com.example.muvitracker.data.utils.mapToIoResponse
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.domain.model.Provider
import com.example.muvitracker.domain.model.base.MovieBase
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.map
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.sortedBy
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


    override suspend fun getMovieProviders(movieId: Int): IoResponse<List<Provider>> {
        // devo ritornare un IoResponse
        return try {
            val dto = tmdbApi.getMovieProviders(movieId)
            IoResponse.Success(getProviderList(dto))
        } catch (ex: CancellationException) {
            // CancellationException - è l’eccezione usata da Kotlin per cancellare una coroutine.
            throw ex // rilancio eccezione -> cancella la coroutines; se non la gestissi separatamente, rompo il meccanismo
        } catch (ex: Throwable) {
            IoResponse.Error(ex)
        }
    }


    private fun getProviderList(response: MovieProvidersResponseDto): List<Provider> {
        //        val region = "IT" // TODO test
//        var responseDto = api.getMovieProviders(293660).results // TODO test
        val region = Locale.getDefault().country
        val regionProvidersDto = response.results[region] ?: return emptyList()

        // 1. unisci le 4 liste come una mappa
        // struttura come mappa key -> lista
        val providersPair = listOf(
            ProviderTypes.BUY to regionProvidersDto.buy,
            ProviderTypes.STREAM to regionProvidersDto.flatrate,
            ProviderTypes.RENT to regionProvidersDto.rent,
            ProviderTypes.FREE to regionProvidersDto.free,
            ProviderTypes.ADS to regionProvidersDto.ads,
        ).filter { pair ->
            pair.second.isNotEmpty() // emptyList default
        }

        // 2. da mappa a Pair<key, Obj>
        // struttura key, elemento singolo
        val flatProvidersPair = providersPair.flatMap { pair ->
            val scomposto = pair.second.map { dto ->
                pair.first to dto // == Pair(pair.first, dto)
            }
            scomposto
        }

        // 3. ragruppa Provider con id identico
        val groupedProvidersMap =
            flatProvidersPair.groupBy { pair ->
                pair.second.providerId
            }

        // 4. (id -> Pair<Type,ProviderDto>)  => List<Provider>
        // creo un provider per ogni entries + unisco i types
        val domainProviders = groupedProvidersMap.map { (id, pairs) ->
            // 1. ragruppo 'type', poi unisco in stringa
            val types = pairs.joinToString(
                separator = ", ",
                transform = {
                    it.first
                })

            val dto = pairs.first().second

            // 2. creo un provider per ogni entry
            val result = Provider(
                providerId = id,
                providerName = dto.providerName ?: "",
                logoPath = dto.logoPath ?: "",
                displayPriority = dto.displayPriority ?: 99, // vai in fondo
                serviceType = types
            )
            result
        }.sortedBy {
            it.displayPriority
        }

        return domainProviders
    }
}


object ProviderTypes {
    var STREAM = MyApp.appContext.getString(R.string.textProviderStream)
    var BUY = MyApp.appContext.getString(R.string.textProviderBuy)
    var RENT = MyApp.appContext.getString(R.string.textProviderRent)
    var ADS = MyApp.appContext.getString(R.string.textProviderAds)
    var FREE = MyApp.appContext.getString(R.string.textProviderFree)

}

/*
            "buy"  - Buy
            "flatrate" - Streaming
            "rent" - Rent
            "free" - Free
            "ads" - Advertising
 */


