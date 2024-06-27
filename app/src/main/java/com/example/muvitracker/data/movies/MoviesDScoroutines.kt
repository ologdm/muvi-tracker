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
import com.example.muvitracker.data.dto.base.MovieDto
import com.example.muvitracker.data.dto.base.toDomain
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.IoResponse2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// TODO - gson in kotlin
// TODO wrappare store in un altra classe, passo vari metodi che servono,


@Singleton
class MoviesDScoroutines @Inject constructor(
    private val gson: Gson, // provides
    private val sharedPreferences: SharedPreferences, // provides
    private val traktApi: TraktApi
) {

    companion object {
        private const val POPULAR_LIST_KEY = "popular_list_key"
        private const val BOXOFFICE_LIST_KEY = "boxoffice_list_key"
    }


    private val popularStore: Store<Unit, List<MovieDto>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult {
            try {
                FetcherResult.Data(traktApi.getPopularMoviesFun())// suspend fun
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Unit, List<MovieDto>, List<MovieDto>>(
            reader = { loadFromSharedFLow() }, // flow
            writer = { _, data -> saveOnShared(data)  } // suspend fun
        )
    ).disableCache()
        .build()


    private fun loadFromSharedFLow(): Flow<List<MovieDto>?> {
        return channelFlow {
            send(readShared())

            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == POPULAR_LIST_KEY) {
                    trySend(readShared())
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }


    private fun readShared(): List<MovieDto>? {
        val jsonString = sharedPreferences.getString(POPULAR_LIST_KEY, null) ?: return null
        return getListFromJson<MovieDto>(jsonString)
    }


    private fun saveOnShared(list: List<MovieDto>) {
        sharedPreferences.edit()
            .putString(POPULAR_LIST_KEY, list.getJson())
            .apply()
    }


    fun getStoreStreamFlow(): Flow<IoResponse2<List<MovieDto>>> {
        return popularStore.stream(StoreRequest.cached(Unit, refresh = true))
            .filterNot { response -> // opposto di filter (escludi)
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }.map { response ->
                when (response) {
                    is StoreResponse.Data -> IoResponse2.Success(response.value)
                    is StoreResponse.Error.Exception -> IoResponse2.Error(response.error)
                    is StoreResponse.Error.Message -> TODO()
                    is StoreResponse.Loading, is StoreResponse.NoNewData -> error("donn't need to be managed") // funzione di kotlin
                }
            }
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


    //    private val json: Json, // koltin
    // TODO json kotlin
//    fun test (){
//        // reified T - con inlinw
//        //
//        Json.encodeToString<MovieDto>(dto, )
//        Json.decodeFromString<MovieDto>()
//    }


}

// flow test
//fun getMoviesFromNetwork(): Flow<IoResponse<List<MovieDto>>> = flow {
//    try {
//        val networkResult = traktApi.getPopularMoviesFlow()
//        emit(IoResponse.Success(networkResult))
//    } catch (ex: CancellationException) {
//        throw ex
//    } catch (ex: Throwable) {
//        emit(IoResponse.NetworkError)
//    }
//}


//private fun loadFromSharedFLow(): Flow<List<MovieDto>> {
//    return flow {
//        emit(loadFromShared())
//        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
//            if (key == MoviesDS_Coroutines.POPULAR_LIST_KEY) {
//                val json = sharedPreferences.getString(MoviesDS_Coroutines.POPULAR_LIST_KEY, null)
//                emit(loadFromShared())
//            }
//        }
//    }
//}



