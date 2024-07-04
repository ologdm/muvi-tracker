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
import com.example.muvitracker.utils.IoResponse2
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class DetailRepositoryTest @Inject constructor(
    private val prefsLocalDS: PrefsLocalDS,
    private val traktApi: TraktApi
) {

    private val cachingMap = mutableMapOf<Int, DetailEntity>()


    // 1 store
    private val detailStore: Store<Int, DetailEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { key ->
            try {
                FetcherResult.Data(traktApi.getMovieDetailTest(key))
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, DetailDto, DetailEntity>(
            reader = { key ->
                // funzione creaflow
                getDetailEntity(key)
            },
            writer = { key, dto ->
                // dto->entity; salvare su cache
                saveDtoToCachingMap(key, dto)
            }
        )
    ).build()


    // 2 funzioni store
    private fun getDetailEntity(id: Int): Flow<DetailEntity?> {
        return flow {
            emit(cachingMap[id])
        }
    }

    private fun saveDtoToCachingMap(id: Int, dto: DetailDto) {
        cachingMap[id] = dto.toEntity()
    }


    // storeFlow + mapping
    // DetailMovie - DetailEntity.toDomain(PrefsEntity)
    fun detailMovieStreamFlow(id: Int): Flow<IoResponse2<DetailMovie>> {
        return detailStore.stream(StoreRequest.cached(key = id, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }.map { response ->
                when (response) {
                    is StoreResponse.Data -> {
                        IoResponse2.Success(response.value)
                            .ioMapper {
                                // TODO - non deve essere nullable
//                                val prefsEntities = prefsLocalDS.liveDataList.value | old
                                val prefsEntities = prefsLocalDS.getPrefsList()
                                val prefsEntity =
                                    prefsEntities?.find { it.movieId == id } // trova solo se != 0
                                it.toDomain(prefsEntity)//
                            }
                    }

                    is StoreResponse.Error.Exception -> IoResponse2.Error(response.error)
                    is StoreResponse.Error.Message -> IoResponse2.Error(RuntimeException(response.message))
                    is StoreResponse.Loading,
                    is StoreResponse.NoNewData -> error("should be filtered upstream")
                }
            }

    }


}








