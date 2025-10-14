package com.example.muvitracker.data.utils

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

// Input, Ouput - generici con momi specifici
// T Any? default; 'Any' not nullable come store richiede

// 1. factory -> for store builder
fun <Key : Any, DtoInput : Any, DomainOutput : Any> storeFactory(
    fetcher: suspend (Key) -> DtoInput,
    reader: (Key) -> Flow<DomainOutput?>,
    writer: suspend (Key, DtoInput) -> Unit
): Store<Key, DomainOutput> {
    return StoreBuilder.from(
        fetcher = Fetcher.ofResult { key ->
            try {
                FetcherResult.Data(fetcher(key))
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Key, DtoInput, DomainOutput>(
            reader = { key ->
                reader(key)
            },
            writer = { key, dtoInput ->
                try {
                    writer(key,dtoInput)
//                    detailShowDao.insertSingle(dto.toEntity())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        )
    )
        .disableCache() // livello cache interno di store
        .build()
}


// 2. extended function for
fun <T> Flow<StoreResponse<T>>.mapToIoResponse(): Flow<IoResponse<T>> {
    return this
        .filterNot { response ->
            response is StoreResponse.Loading || response is StoreResponse.NoNewData
        }
        .map { storeResponse ->
            when (storeResponse) {
                is StoreResponse.Data -> {
                    val data = storeResponse.value
                    IoResponse.Success(data)
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