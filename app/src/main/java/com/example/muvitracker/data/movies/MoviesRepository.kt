package com.example.muvitracker.data.movies

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.BoxoMovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.BoxoDto
import com.example.muvitracker.data.dto.basedto.toEntity
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.utils.IoResponse2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

// for lists without paging

class MoviesRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase
) : MoviesRepo {

    private val boxofficeDao = database.boxofficeDao()

    // OK
    private val boxofficeStore: Store<Unit, List<BoxoMovieEntity>> = StoreBuilder.from(
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
        sourceOfTruth = SourceOfTruth.of<Unit, List<BoxoDto>, List<BoxoMovieEntity>>(
            reader = { _ ->
                boxofficeDao.readAll()
            },
            writer = { _, list ->
                boxofficeDao.insertList(list.map { it.toEntity() })
            }
        )
    ).build()


    override
    fun getBoxoStoreStream(): Flow<IoResponse2<List<Movie>>> {
        return boxofficeStore.stream(StoreRequest.cached(key = Unit, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }
            .map { storeResponse ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        IoResponse2.Success(storeResponse.value.map { it.toDomain() })
                    }

                    is StoreResponse.Error.Exception -> {
                        IoResponse2.Error(storeResponse.error)
                    }

                    is StoreResponse.Error.Message -> {
                        IoResponse2.Error(RuntimeException(storeResponse.message))
                    }

                    is StoreResponse.Loading,
                    is StoreResponse.NoNewData -> error("should be filtered upstream")
                }
            }
    }


}