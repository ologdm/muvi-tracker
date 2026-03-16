package com.example.data.utils

import com.example.domain.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadResponse

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
                    writer(key, dtoInput)
//                    detailShowDao.insertSingle(dto.toEntity())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        )
    )
        .disableCache() // livello cache interno di store
        .build() as Store<Key, DomainOutput>
}


// 2. extended function for
fun <T> Flow<StoreReadResponse<T>>.mapToIoResponse(): Flow<IoResponse<T>> {
    return this
        .filterNot { response ->
            response is StoreReadResponse.Loading || response is StoreReadResponse.NoNewData
        }
        .map { storeResponse ->
            when (storeResponse) {
                is StoreReadResponse.Data -> {
                    val data = storeResponse.value
                    IoResponse.Success(data)
                }

                is StoreReadResponse.Error.Exception -> {
                    IoResponse.Error(storeResponse.error)
                }

                is StoreReadResponse.Error.Message -> {
                    IoResponse.Error(RuntimeException(storeResponse.message))
                }

                is StoreReadResponse.Loading,
                is StoreReadResponse.NoNewData -> error("should be filtered upstream")
            }
        }
}