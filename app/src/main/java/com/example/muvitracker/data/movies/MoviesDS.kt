package com.example.muvitracker.data.movies

import android.content.SharedPreferences
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.BoxoDto
import com.example.muvitracker.data.dto.base.MovieDto
import com.example.muvitracker.utils.IoResponse2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// TODO - gson in kotlin
// TODO wrappare store in un altra classe, passo vari metodi che servono


@Singleton
class MoviesDS @Inject constructor(
    private val gson: Gson, // provides
    private val sharedPreferences: SharedPreferences, // provides
    private val traktApi: TraktApi // provides
) {

    companion object {
        private const val POPULAR_LIST_KEY = "popular_list_key"
        private const val BOXOFFICE_LIST_KEY = "boxoffice_list_key"
    }



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
            reader = { getPopularFLow() }, // flow
            writer = { _, data -> savePopularOnShared(data) } // suspend fun
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
            reader = { getBoxoFLow() },
            writer = { _, data -> saveBoxoOnShared(data) }
        )
    ).disableCache()
        .build()


    // POPULAR ######################################################################
    fun getPopularStoreStream(): Flow<IoResponse2<List<MovieDto>>> {
        return popularStore.stream(StoreRequest.cached(Unit, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }.map { response ->
                when (response) {
                    is StoreResponse.Data -> IoResponse2.Success(response.value)
                    is StoreResponse.Error.Exception -> IoResponse2.Error(response.error)
                    is StoreResponse.Error.Message -> IoResponse2.Error(RuntimeException(response.message))// msg encapsulated into throwable
                    is StoreResponse.Loading, is StoreResponse.NoNewData -> error("should be filtered upstream") // error - kotlin function
                }
            }
    }

    private fun getPopularFLow(): Flow<List<MovieDto>?> { // flow con listener cambiamenti su shared
        return channelFlow {
            send(readPopularShared())

            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == POPULAR_LIST_KEY) {
                    trySend(readPopularShared())
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }


    private fun readPopularShared(): List<MovieDto>? {
        val jsonString = sharedPreferences.getString(POPULAR_LIST_KEY, null) ?: return null
        return getListFromJson<MovieDto>(jsonString)
    }


    private fun savePopularOnShared(list: List<MovieDto>) {
        sharedPreferences.edit()
            .putString(POPULAR_LIST_KEY, list.getJson())
            .apply()
    }


    // BOXO ######################################################################
    fun getBoxoStoreStream(): Flow<IoResponse2<List<BoxoDto>>> {
        return boxoStore.stream(StoreRequest.cached(Unit, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }.map { response ->
                when (response) {
                    is StoreResponse.Data -> {
                        println("XXX STATE DS STREAM: ${response.value}")
                        IoResponse2.Success(response.value)
                    }

                    is StoreResponse.Error.Exception -> IoResponse2.Error(response.error)
                    is StoreResponse.Error.Message -> IoResponse2.Error(RuntimeException(response.message))
                    is StoreResponse.Loading, is StoreResponse.NoNewData -> error("should be filtered upstream")
                }
            }
    }

    private fun getBoxoFLow(): Flow<List<BoxoDto>?> {
        return channelFlow {
            send(readBoxoShared())

            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == BOXOFFICE_LIST_KEY) {
                    trySend(readBoxoShared())
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }


    private fun readBoxoShared(): List<BoxoDto>? {
        val jsonString = sharedPreferences.getString(BOXOFFICE_LIST_KEY, null) ?: return null
        return getListFromJson<BoxoDto>(jsonString)
    }


    private fun saveBoxoOnShared(list: List<BoxoDto>) {
        sharedPreferences.edit()
            .putString(BOXOFFICE_LIST_KEY, list.getJson())
            .apply()
    }


    // CONVERTERS ##################################################################
    private inline fun <reified T> getListFromJson(jsonString: String?): List<T> {
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            val listType = object : TypeToken<List<T>>() {}.type // standard
            gson.fromJson(jsonString, listType)
        }
    }

    private fun <T> List<T>.getJson(): String {
        return gson.toJson(this)
    }

}
