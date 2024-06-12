package com.example.muvitracker.data.movies

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.domain.model.base.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

// private val gson: Gson, // convertire a Serializable

@Singleton
class MoviesLocalDS @Inject constructor(
    private val gson: Gson, // provides
    private val sharedPreferences: SharedPreferences // provides
) {


    // GETTER ###########################################################################

    fun getPopularLivedataList(): LiveData<List<Movie>> {
        val livedata = MutableLiveData<List<Movie>>()
        livedata.value = loadPopularFromShared()
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == POPULAR_LIST_KEY) {
                livedata.value = loadPopularFromShared()
            }
        }
        return livedata
    }


    private fun loadPopularFromShared(): List<Movie> {
        val jsonString = sharedPreferences.getString(POPULAR_LIST_KEY, null)
        return getListFromJson<Movie>(jsonString ?: "").orEmpty() // TODO string ""
    }


    fun getBoxoLivedataList(): LiveData<List<Movie>> {
        val livedata = MutableLiveData<List<Movie>>()
        livedata.value = loadBoxoFromShared()
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == BOXOFFICE_LIST_KEY) {
                livedata.value = loadBoxoFromShared()
            }
        }
        return livedata
    }


    private fun loadBoxoFromShared(): List<Movie> {
        var jsonString = sharedPreferences.getString(BOXOFFICE_LIST_KEY, null)
        return getListFromJson<Movie>(jsonString ?: "").orEmpty()
    }


    // SET ###########################################################################

    fun savePopularInLocal(list: List<Movie>) {
        sharedPreferences.edit()
            .putString(POPULAR_LIST_KEY, list.getJson())
            .commit()
    }

    fun saveBoxoInLocal(list: List<Movie>) {
        sharedPreferences.edit()
            .putString(BOXOFFICE_LIST_KEY, list.getJson())
            .commit()
    }


    // CONVERTERS ##################################################################

    // TODO kotlin style
    private inline fun <reified T> getListFromJson(jsonString: String): List<T>? {
        val listType = object : TypeToken<List<T>>() {}.type
        val list: List<T> = gson.fromJson(jsonString, listType) ?: emptyList()
        return list
    }

    // TODO kotlin style
    private fun <T> List<T>.getJson(): String {
        return gson.toJson(this)
    }


    // ##################################################################
    companion object {
        private const val POPULAR_LIST_KEY = "popular_list_key"
        private const val BOXOFFICE_LIST_KEY = "boxoffice_list_key"
    }
}


//        private var instance: MoviesLocalDS? = null
//        fun getInstance(context: Context): MoviesLocalDS {
//            if (instance == null) {
//                instance = MoviesLocalDS(context)
//            }
//            return instance!!
//        }

