package com.example.muvitracker.data.movies


import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.BoxoDto
import com.example.muvitracker.data.dto.basedto.MovieDto
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.data.dto.basedto.toDomain
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.utils.IoResponse2
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Singleton
class MoviesRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val moviesDS: MoviesDS
) : MoviesRepo {


    // store builders #################################################################
    private val popularStore: Store<Unit, List<MovieDto>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult {
            try {
                FetcherResult.Data(traktApi.getPopularMoviesTest())// suspend fun
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Unit, List<MovieDto>, List<MovieDto>>(
            reader = { moviesDS.getPopularFLow() }, // flow
            writer = { _, data -> moviesDS.savePopularOnShared(data) } // suspend fun
        )
    ).disableCache()
        .build()


    private val boxoStore: Store<Unit, List<BoxoDto>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult {
            try {
                FetcherResult.Data(traktApi.getBoxoMoviesTest())
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


    // contract funs - with store stream
    override
    fun getPopularStoreStream(): Flow<IoResponse2<List<Movie>>> {
        return popularStore.stream(StoreRequest.cached(Unit, refresh = true))
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
                    is StoreResponse.Error.Message -> IoResponse2.Error(RuntimeException(response.message))// msg encapsulated into throwable
                    is StoreResponse.Loading, is StoreResponse.NoNewData -> error("should be filtered upstream") // error - kotlin function
                }
            }
    }


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







