package com.example.muvitracker.data.movies


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.PopularMovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.BoxoDto
import com.example.muvitracker.data.dto.basedto.MovieDto
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.data.dto.basedto.toDomain
import com.example.muvitracker.data.dto.basedto.toEntity
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.ui.main.allmovies.base.PopularPagingSource
import com.example.muvitracker.utils.IoResponse2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// room
// paging in room

@Singleton
class MoviesRepositoryR @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase, // TODO OK
    private val moviesDS: MoviesDS
) : MoviesRepo {


//    fun popularPagingSource() = PopularPagingSource(traktApi)


    override
    val popularPager = Pager(
        config = PagingConfig(pageSize = 15, enablePlaceholders = false),
        pagingSourceFactory = { PopularPagingSource(traktApi) }
    ).flow
        .map {
            it.map { dto ->
                dto.toDomain()
            }
        }



    // TODO 1
//    fun getData(): Pager<Int, Movie> {
//        val config = PagingConfig(
//            pageSize = 50, // numero elementi per pagina
//            enablePlaceholders = true
//        )
//
//        return Pager(
//            config,
//            pagingSourceFactory = {PopularPagingSource(traktApi)}
//        ).flow.map {
//            it.map { it.toDomain() }
//        }
//    }


    // TODO versione 2
    fun getDataStream(): Flow<PagingData<Movie>> {
        val config = PagingConfig(pageSize = 50, enablePlaceholders = false)

        return Pager(
            config,
            pagingSourceFactory = { PopularPagingSource(traktApi) }
        ).flow.map {
            it.map { it.toDomain() }
        }
    }


    // TODO OK
    private val popularDao = database.popularDao()
    private val boxofficeDao = database.boxofficeDao()


    private val popularStore: Store<Unit, List<PopularMovieEntity>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult {
            try {
                FetcherResult.Data(traktApi.getPopularMovies())// suspend fun
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Unit, List<MovieDto>, List<PopularMovieEntity>>(
            reader = {
                popularDao.readAll() // TODO room
            }, // flow
            writer = { _, data -> // TODO room
                popularDao.insertList(data.map { dto ->
                    dto.toEntity()
                })
            } // suspend fun
        )
    ).disableCache()
        .build()


    override
    fun getPopularStoreStream(): Flow<IoResponse2<List<Movie>>> {
        return popularStore.stream(StoreRequest.cached(Unit, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }.map { response ->
                when (response) {
                    is StoreResponse.Data -> {
                        IoResponse2.Success(response.value.map { entity ->
                            entity.toDomain()
                        })
                    }

                    is StoreResponse.Error.Exception -> IoResponse2.Error(response.error)
                    is StoreResponse.Error.Message -> IoResponse2.Error(RuntimeException(response.message))// msg encapsulated into throwable
                    is StoreResponse.Loading, is StoreResponse.NoNewData -> error("should be filtered upstream") // error - kotlin function
                }
            }
    }


// TODO ######################################################################################

    private val boxoStore: Store<Unit, List<BoxoDto>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult {
            try {
                FetcherResult.Data(traktApi.getBoxoMovies())
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Unit, List<BoxoDto>, List<BoxoDto>>(
            reader = { moviesDS.getBoxoFLow() },
            writer = { _, data -> moviesDS.saveBoxoOnShared(data) }
        )
    ).disableCache()
        .build()


    override
    fun getBoxoStoreStream(): Flow<IoResponse2<List<Movie>>> {
        return boxoStore.stream(StoreRequest.cached(Unit, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }.map { response ->
                when (response) {
                    is StoreResponse.Data -> {
                        IoResponse2.Success(response.value.map { dto ->
                            dto.toDomain()
                        })
                    }

                    is StoreResponse.Error.Exception -> IoResponse2.Error(response.error)
                    is StoreResponse.Error.Message -> IoResponse2.Error(RuntimeException(response.message))
                    is StoreResponse.Loading, is StoreResponse.NoNewData -> error("should be filtered upstream")
                }
            }
    }


}







