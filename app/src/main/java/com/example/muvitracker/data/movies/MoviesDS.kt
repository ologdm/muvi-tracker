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
import com.example.muvitracker.data.dto.basedto.MovieDto
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
) {

    companion object {
        private const val POPULAR_LIST_KEY = "popular_list_key"
        private const val BOXOFFICE_LIST_KEY = "boxoffice_list_key"
    }


    // GET FLOW
    fun getPopularFLow(): Flow<List<MovieDto>?> { // flow con listener cambiamenti su shared
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

    fun getBoxoFLow(): Flow<List<BoxoDto>?> {
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


    // READ - private
    private fun readPopularShared(): List<MovieDto>? {
        val jsonString = sharedPreferences.getString(POPULAR_LIST_KEY, null) ?: return null
        return getListFromJson<MovieDto>(jsonString)
    }

    private fun readBoxoShared(): List<BoxoDto>? {
        val jsonString = sharedPreferences.getString(BOXOFFICE_LIST_KEY, null) ?: return null
        return getListFromJson<BoxoDto>(jsonString)
    }


    // SAVE
    fun savePopularOnShared(list: List<MovieDto>) {
        sharedPreferences.edit()
            .putString(POPULAR_LIST_KEY, list.getJson())
            .apply()
    }


    fun saveBoxoOnShared(list: List<BoxoDto>) {
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
