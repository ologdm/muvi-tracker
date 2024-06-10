package com.example.muvitracker.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PrefsLocalDS(
    val context: Context
) {
    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("prefs_cache", Context.MODE_PRIVATE)


    // GET ###################################################### ZZ modificato e synchronized
    private val prefsChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == PREFS_KEY_01) {
                liveDataList.postValue(loadSharedList())
            }
        }


    val liveDataList: MutableLiveData<List<PrefsEntity>> by lazy {
        MutableLiveData<List<PrefsEntity>>().also {
            it.value = loadSharedList()
            sharedPreferences.registerOnSharedPreferenceChangeListener(prefsChangeListener)
        }
    }


    private fun loadSharedList(): List<PrefsEntity> {
        val json =
            sharedPreferences.getString(PREFS_KEY_01, "").orEmpty()
        return getListFromJson(json)
    }


    private fun getListFromJson(jsonString: String): List<PrefsEntity> {
        var listType = object : TypeToken<List<PrefsEntity>>() {}.type
        return gson.fromJson(jsonString, listType) ?: emptyList()
    }


    // SET ###################################################### ZZ
    fun toggleFavoriteOnDB(id: Int) {
        synchronized(this) {
            val cache = loadSharedList().toMutableList()
            val index = cache.indexOfFirst { it.movieId == id }
            if (index != -1) {
                val current = cache[index]
                cache.set(index, current.copy(liked = !current.liked))
            } else {
                cache.add(PrefsEntity(liked = true, watched = false, movieId = id))
            }
            saveListInShared(cache)
        }
    }


    fun updateWatchedOnDB(id: Int, watched: Boolean) {
        synchronized(this) {
            val cache = loadSharedList().toMutableList()
            val index = cache.indexOfFirst { it.movieId == id }
            if (index != -1) {
                val current = cache[index]
                cache.set(index, current.copy(watched = watched))
            } else {
                cache.add(PrefsEntity(liked = false, watched = watched, movieId = id))
            }
            saveListInShared(cache)
        }
    }


// ########################################################################### ZZ

    fun saveListInShared(list: List<PrefsEntity>) {
        synchronized(this) {
            sharedPreferences.edit()
                .putString(PREFS_KEY_01, getJson(list))
                .commit()
        }
    }


    private fun getJson(list: List<PrefsEntity>): String {
        return gson.toJson(list) ?: ""
    }


    // ###########################################################################
    companion object {
        private const val PREFS_KEY_01 = "prefs_key_01"

        private var instance: PrefsLocalDS? = null
        fun getInstance(context: Context): PrefsLocalDS {
            if (instance == null) {
                instance = PrefsLocalDS(context)
            }
            return instance!!
        }
    }

}
