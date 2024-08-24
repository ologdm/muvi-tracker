package com.example.muvitracker.data

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.data.database.entities.PrefsEntity
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.DetailShowDto
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// solo per detail fragment
// 1. detail base
// 2. season


@Singleton
class DetailShowRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase
) {

    private val detailShowDao = database.detailShowDao()
    private val prefsShowDao = database.prefsShowDao()


    // DETAIL STORE ########################################################### 00
    private val detailStore: Store<Int, DetailShowEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { key ->
            try {
                FetcherResult.Data(traktApi.getShowDetail(key))

            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, DetailShowDto, DetailShowEntity>(
            reader = { key ->
                getDetailShowEntity(key)
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

    fun testx() {
        detailStore.stream(StoreRequest.)
    }


    // 00
    private fun getDetailShowEntity(id: Int): Flow<DetailShowEntity?> {
        return detailShowDao.readSingleFlow(id) // with flow, observable db
    }

    // 00
    private suspend fun saveDtoToDatabase(dto: DetailShowDto) {
        detailShowDao.insertSingle(dto.toEntity()) // suspend dao fun
    }


    // CONTRACT METHODS
//    override  00
    fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse<DetailShow>> {
        // flow1
        val detailFlow = detailStore.stream(StoreRequest.cached(key = id, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }
        // flow2
        val prefsListFLow = prefsShowDao.readAll()

        // combine flows T1, T2 -> R
        return detailFlow
            .combine(prefsListFLow) { storeResponse, prefList ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        val prefsEntity = prefList.find { entity ->
                            entity?.traktId == storeResponse.value.ids.trakt
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


    // 00
    suspend fun toggleLikedOnDb(id: Int) {
        val entity = prefsShowDao.readSingle(id).firstOrNull()// flow closing function
        if (entity != null) {
            prefsShowDao.updateLiked(id)
        } else {
            prefsShowDao.insertSingle(
                PrefsShowEntity(
                    traktId = id,
                    liked = true,
                    watchedAll = false,
                    watchedCount = 0,
                    addedDateTime = System.currentTimeMillis()
                )
            )
        }
    }


    // SEASONS STORE ###########################################################

}
