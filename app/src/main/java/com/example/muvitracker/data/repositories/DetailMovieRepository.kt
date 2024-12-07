package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.DetailMovieDto
import com.example.muvitracker.data.dto.movie.toDomain
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Singleton
class DetailMovieRepository @Inject constructor(
    private val traktApi: TraktApi,
    database: MyDatabase
) : DetailRepo {

    private val detailDao = database.detailMovieDao()
    private val prefsDao = database.prefsMovieDao()

// old
//    private val olddetailStore: Store<Int, DetailMovieEntity> = StoreBuilder.from(
//        fetcher = Fetcher.ofResult { key ->
//            try {
//                FetcherResult.Data(traktApi.getMovieDetail(key))
//
//            } catch (ex: CancellationException) {
//                throw ex
//            } catch (ex: Throwable) {
//                ex.printStackTrace()
//                FetcherResult.Error.Exception(ex)
//            }
//        },
//        sourceOfTruth = SourceOfTruth.of<Int, DetailMovieDto, DetailMovieEntity>(
//            reader = { key ->
//                getDetailEntity(key)
//            },
//            writer = { _, dto ->
//                try {
//                    saveDtoToDatabase(dto)
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//
//            }
//        )
//    ).build()

    // no wrap todo
    private val store = storeFactory<Int, DetailMovieDto, DetailMovieEntity>(
        fetcher = { movieId ->
            traktApi.getMovieDetail(movieId)
        },
        reader = { movieId ->
            detailDao.readSingleFlow(movieId)
        },
        writer = { _, movieDto ->
            detailDao.insertSingle(movieDto.toEntity())
        }
    )

    // senza wrap todo
    // CONTRACT METHODS
    // for detail view
    override
    fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse<DetailMovie>> {
        // flow1
        val detailFlow = store.stream(StoreRequest.cached(key = id, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }
        // flow2
        val prefsListFLow = prefsDao.readAll()

        // combine flows T1, T2 -> R
        return detailFlow
            .combine(prefsListFLow) { storeResponse, prefList ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        val prefsEntity = prefList.find { entity ->
                            entity.traktId == storeResponse.value.ids.trakt
                        }
                        val detailMovie = storeResponse.value.toDomain(prefsEntity)
                        IoResponse.Success(detailMovie)
                    }

                    is StoreResponse.Error.Exception ->
                        IoResponse.Error(storeResponse.error)

                    is StoreResponse.Error.Message ->
                        IoResponse.Error(RuntimeException(storeResponse.message))

                    is StoreResponse.Loading,
                    is StoreResponse.NoNewData -> error("should be filtered upstream")
                }
            }
    }


    // for prefs view
    override
    fun getDetailListFlow(): Flow<List<DetailMovieEntity?>> {
        return detailDao.readAllFlow()
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
