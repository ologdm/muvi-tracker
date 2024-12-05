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
import com.example.muvitracker.data.database.entities.BoxofficeMovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.movie.BoxofficeDtoM
import com.example.muvitracker.data.dto.movie.toEntity
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// for lists without paging

@Singleton
class MoviesRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase
) : MoviesRepo {


    private val boxofficeDao = database.boxofficeDao()

    // OK
    private val boxofficeStore: Store<Unit, List<BoxofficeMovieEntity>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { key ->
            try {
                FetcherResult.Data(traktApi.getBoxoMovies())

            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Unit, List<BoxofficeDtoM>, List<BoxofficeMovieEntity>>(
            reader = { _ ->
                boxofficeDao.readAll()
                    .map { if (it.isEmpty()) null else it }
            },
            writer = { _, list ->
                boxofficeDao.insertList(list.map { it.toEntity() })
            }
        )
    ).disableCache()
        .build()



    override
    fun getBoxoStoreStream(): Flow<IoResponse<List<Movie>>> {
        return boxofficeStore.stream(StoreRequest.cached(key = Unit, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }
            .map { storeResponse ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        IoResponse.Success(storeResponse.value.map { it.toDomain() })
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

}