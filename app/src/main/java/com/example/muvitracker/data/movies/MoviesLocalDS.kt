package com.example.muvitracker.data.movies

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.domain.model.base.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MoviesLocalDS
private constructor(
    context: Context
) {

    private val gson = Gson() // TODO convertire a Serializable
    private val sharedPrefefences: SharedPreferences =
        context.getSharedPreferences("all_movies_list", Context.MODE_PRIVATE)


    // GETTER ###########################################################################

    fun getPopularLivedataList(): LiveData<List<Movie>> {
        val livedata = MutableLiveData<List<Movie>>()
        livedata.value = loadPopularFromShared()
        sharedPrefefences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == POPULAR_LIST_KEY) {
                livedata.value = loadPopularFromShared()
            }
        }
        return livedata
    }


    private fun loadPopularFromShared(): List<Movie> {
        val jsonString = sharedPrefefences.getString(POPULAR_LIST_KEY, null)
        return getListFromJson<Movie>(jsonString ?: "").orEmpty() // TODO string ""
    }


    fun getBoxoLivedataList(): LiveData<List<Movie>> {
        val livedata = MutableLiveData<List<Movie>>()
        livedata.value = loadBoxoFromShared()
        sharedPrefefences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == BOXOFFICE_LIST_KEY) {
                livedata.value = loadBoxoFromShared()
            }
        }
        return livedata
    }


    private fun loadBoxoFromShared(): List<Movie> {
        var jsonString = sharedPrefefences.getString(BOXOFFICE_LIST_KEY, null)
        return getListFromJson<Movie>(jsonString ?: "").orEmpty()
    }


    // SET ###########################################################################

    fun savePopularInLocal(list: List<Movie>) {
        sharedPrefefences.edit()
            .putString(POPULAR_LIST_KEY, list.getJson())
            .commit()
    }

    fun saveBoxoInLocal(list: List<Movie>) {
        sharedPrefefences.edit()
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

        private var instance: MoviesLocalDS? = null
        fun getInstance(context: Context): MoviesLocalDS {
            if (instance == null) {
                instance = MoviesLocalDS(context)
            }
            return instance!!
        }


    }
}



