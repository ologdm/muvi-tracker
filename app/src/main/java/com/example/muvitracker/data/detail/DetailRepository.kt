package com.example.muvitracker.data.detail

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.data.prefs.PrefsLocalDS
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.utils.IoResponse2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class DetailRepository @Inject constructor(
    private val prefsLocalDS: PrefsLocalDS,
    private val traktApi: TraktApi,
) : DetailRepo {

    // TODO - migrate to room
    private val cachingMap = mutableMapOf<Int, DetailEntity>()


    // 1 store
    private val detailStore: Store<Int, DetailEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { key ->
            try {
                FetcherResult.Data(traktApi.getMovieDetailTest(key))
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, DetailDto, DetailEntity>(
            reader = { key ->
                getDetailEntity(key)
            },
            writer = { key, dto ->
                // dto->entity; save on cache
                saveDtoToCacheMap(key, dto)

            }
        )
    ).build()


    // STORE FUNCTIONS
    private fun getDetailEntity(id: Int): Flow<DetailEntity?> {
        return flow {
            emit(cachingMap[id])
        }
    }

    private fun saveDtoToCacheMap(id: Int, dto: DetailDto) {
        cachingMap[id] = dto.toEntity()
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
        val prefsListFLow = prefsLocalDS.getPrefsListFlow()

        // combine flows T1, T2 -> R
        return detailFlow
            .combine(prefsListFLow) { storeResponse, prefList ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        val prefsEntity = prefList.find { entity ->
                            entity.movieId == storeResponse.value.ids.trakt
                        }
                        // puo creare domain con o senza prefs
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
    fun getDetailListFlow(): Flow<List<DetailEntity>> {
        return flow {
            emit(cachingMap.values.toList())
        }
    }

}

