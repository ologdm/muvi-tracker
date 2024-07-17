package com.example.muvitracker.data.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefsLocalDS @Inject constructor(
    private val gson: Gson,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val PREFS_KEY_01 = "prefs_key_01"
    }


    fun getPrefsListFlow(): Flow<List<PrefsEntity>> {
        return channelFlow {
            send(readSharedPreferences())

            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == PREFS_KEY_01) {
                    trySend(readSharedPreferences())
                }
            }

            sharedPreferences
                .registerOnSharedPreferenceChangeListener(listener)

            awaitClose {
                sharedPreferences
                    .unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }


    // READ/ SAVE LIST
    private fun readSharedPreferences(): List<PrefsEntity> {
        val json =
            sharedPreferences.getString(PREFS_KEY_01, "").orEmpty()
        return getListFromJson(json)
    }

    private fun saveListInShared(list: List<PrefsEntity>) {
            sharedPreferences.edit()
                .putString(PREFS_KEY_01, getJson(list))
                .commit()

    }


    // SET SINGLE ELEMENT ######################################################
    fun toggleFavoriteOnDB(movieId: Int) {
            val cache = readSharedPreferences().toMutableList()
            val index = cache.indexOfFirst { it.movieId == movieId }
            if (index != -1) {
                val current = cache[index]
                cache[index] = current.copy(liked = !current.liked)
            } else {
                cache.add(PrefsEntity(liked = true, watched = false, movieId = movieId))
            }
            saveListInShared(cache)
    }


    fun updateWatchedOnDB(movieId: Int, watched: Boolean) {
            val cache = readSharedPreferences().toMutableList()
            val index = cache.indexOfFirst { it.movieId == movieId }
            if (index != -1) {
                val current = cache[index]
                cache[index] = current.copy(watched = watched)
            } else {
                cache.add(PrefsEntity(liked = false, watched = watched, movieId = movieId))
            }
            saveListInShared(cache)
    }


    fun deleteItemFromDB(movieId: Int) {
            val cache = readSharedPreferences().toMutableList()
            val index = cache.indexOfFirst { it.movieId == movieId }
            if (index != -1) {
                cache.removeAt(index)
            }
            saveListInShared(cache)
    }



    // CONVERTERS ##################################################################
    private fun getListFromJson(jsonString: String): List<PrefsEntity> {
        val listType = object : TypeToken<List<PrefsEntity>>() {}.type
        return gson.fromJson(jsonString, listType) ?: emptyList()
    }

    private fun getJson(list: List<PrefsEntity>): String {
        return gson.toJson(list) ?: ""
    }




}



