package com.example.muvitracker.data.detail

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.DetailEntityR
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.data.dto.toEntityR
import com.example.muvitracker.data.prefs.PrefsLocalDS
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.utils.IoResponse2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Singleton
class DetailRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase
) : DetailRepo {

    private val detailDao = database.detailDao()
    private val prefsDao = database.prefsDao()


    private val detailStore: Store<Int, DetailEntityR> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { key ->
            try {
                FetcherResult.Data(traktApi.getMovieDetail(key))
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, DetailDto, DetailEntityR>(
            reader = { key ->
                // catch before collect
                getDetailEntity(key)
            },
            writer = { _, dto ->
                try {
                    saveDtoToDatabase(dto)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        )
    ).build()


    private fun getDetailEntity(id: Int): Flow<DetailEntityR?> {
        return detailDao.readSingleFlow(id) // with flow, observable db
    }

    private suspend fun saveDtoToDatabase(dto: DetailDto) {
        detailDao.insertSingle(dto.toEntityR()) // suspend dao fun
    }


    // CONTRACT METHODS
    // for detail view
    override
    fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse2<DetailMovie>> {
        // flow1
        val detailFlow = detailStore.stream(StoreRequest.cached(key = id, refresh = true))
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
                        entity?.traktId == storeResponse.value.ids.trakt
                    }
                    val detailMovie = storeResponse.value.toDomain(prefsEntity)
                    IoResponse2.Success(detailMovie)
                }

                is StoreResponse.Error.Exception ->
                    IoResponse2.Error(storeResponse.error)

                is StoreResponse.Error.Message ->
                    IoResponse2.Error(RuntimeException(storeResponse.message))

                is StoreResponse.Loading,
                is StoreResponse.NoNewData -> error("should be filtered upstream")
            }

        }
    }


    // for prefs view
    override
    fun getDetailListFlow(): Flow<List<DetailEntityR?>> {
        return detailDao.readAllFlow()
    }

}
