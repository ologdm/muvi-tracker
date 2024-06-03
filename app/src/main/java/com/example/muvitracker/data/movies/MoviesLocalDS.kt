package com.example.muvitracker.data.movies

import android.content.Context
import android.content.SharedPreferences
import com.example.muvitracker.data.dto.BoxoDto
import com.example.muvitracker.data.dto.base.MovieDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * ### SHARED PREFS MANAGMENT ###
 * id necessari shared prefs:
 *  - "myMoviesPrefs"   => id fase creazione prefs
 *  - "chiaveLista01" => id popu list
 *  - "chiaveLista02" => id boxo list
 *
 * FUNZIONI:
 * conversion:
 *  1) pr fun getJson
 *  2) pr fun getListFromJson
 *
 * metodi get/set list:
 *  1) fun saveListInLocal
 *  2) fun loadFromLocal
 *
 * null management:
 *     > loadFromLocal(), json null -> lista null
 *     > getListFromJson(), json null,
 */

// koltin news
// inline - <refired T>

// TODO convertire a Serializable

class MoviesLocalDS
private constructor(
    private val context: Context
) {

    private val gson = Gson()
    private val sharedPrefefencesDB: SharedPreferences =
        context.getSharedPreferences("myMoviesPrefs", Context.MODE_PRIVATE)


    // METODI CONVERSION --------------------
    // 1. List -> Json (generico)
    private fun <T> List<T>.getJson(): String {
        return gson.toJson(this)
    }

    // (eugi)
    // 2. Json -> List
    // (to make it T, inline + reified is compulsory)
//    private fun < T> getListFromJson(jsonString: String): List<T>? {
//        try {
//            val listType = object : TypeToken<List<T>>() {}.type
//            val list = gson.fromJson<List<T>>(jsonString, listType)
//            return list
//        } catch (ex: Throwable) {
//            ex.printStackTrace()
//            return emptyList()
//        }
//    }

    // funzionante - TODO modificare con serializable
    private inline fun <reified T> getListFromJson(jsonString: String): List<T>? {
            val listType = object : TypeToken<List<T>>() {}.type
            val list: List<T> = gson.fromJson(jsonString, listType) ?: emptyList()
            return list
    }


    // SETTER -------------------------------
    fun savePopularInLocal(list: List<MovieDto>) {
        // aggiungi lista a DB e converti a json
        sharedPrefefencesDB.edit()
            .putString(POPULAR_LIST_KEY, list.getJson())
            .apply()
    }

    fun saveBoxoInLocal(list: List<BoxoDto>) {
        sharedPrefefencesDB.edit()
            .putString(BOXOFFICE_LIST_KEY, list.getJson())
            .apply()
    }


    // GETTER -------------------------------
    // !!! string "" sbagliata
    fun loadPopularFromLocal(): List<MovieDto> {
        var jsonString = sharedPrefefencesDB.getString(POPULAR_LIST_KEY, null)
        var localList = getListFromJson<MovieDto>(jsonString ?: "")   //string vuota -> default ""
        return localList ?: emptyList()   //se stringaDb null => lista null
    }


    fun loadBoxoFromLocal(): List<BoxoDto> {
        var jsonString = sharedPrefefencesDB.getString(BOXOFFICE_LIST_KEY, null)
        var localList = getListFromJson<BoxoDto>(jsonString ?: "")
        return localList ?: emptyList()
    }


    companion object {
        private var instance: MoviesLocalDS? = null
        fun getInstance(context: Context): MoviesLocalDS {
            if (instance == null) {
                instance = MoviesLocalDS(context)
            }
            return instance!!
        }

        private const val POPULAR_LIST_KEY = "chiaveLista01"
        private const val BOXOFFICE_LIST_KEY = "chiaveLista02"
    }
}


// OLD
//    // per popular
//    private fun getListFromJson(jsonString: String): List<PopuDto>? {
//        // 1 configura tipo di ritorno da json
//        var listType = object : TypeToken<List<PopuDto>>() {}.type
//
//        // 2 trasforma json in lista
//        var populist: List<PopuDto>? = gson.fromJson(jsonString, listType) ?: listOf()
//
//        println("XXX_POPU_LOCAL_CONVERT_JSONTOLIST OK")
//        return populist
//    }

//    private fun getJson(list: List<PopuDto>): String {
//        var stringJson = gson.toJson(list)
//        return stringJson
//    }


