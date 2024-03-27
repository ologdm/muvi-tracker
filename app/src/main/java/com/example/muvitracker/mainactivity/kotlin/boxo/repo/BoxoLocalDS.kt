package com.example.muvitracker.mainactivity.kotlin.boxo.repo

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.muvitracker.repo.kotlin.dto.BoxoDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * ### SHARED PREFS MANAGMENT ###
 *
 * - identico a popular
 * - cache su sharedprefs
 * singleton con context
 *
 *
 * FUNZIONI:
 *  conversion
 *  get/set su shared
 *
 *
 */


class BoxoLocalDS
private constructor(

    private val context: Context

) {

    // ATTRIBUTI
    private val gson = Gson()
    private val boxoSharedPrefs: SharedPreferences =
        context.getSharedPreferences("myBoxoPrefs", Context.MODE_PRIVATE)


    // SINGLETON
    companion object {

        @Volatile
        @SuppressLint("StaticFieldLeak")
        private var instance: BoxoLocalDS? = null


        fun getInstance(context: Context): BoxoLocalDS {
            instance ?: synchronized(this) {
                instance ?: BoxoLocalDS(context.applicationContext)
                    .also {
                        instance = it
                    }
            }
            return instance!!
        }

        // costante
        private const val BOXO_LIST_01 = "chiaveLista01"
    }


    // METODI CONVERSIONE OK

    // 1 List -> Json
    private fun getJson(list: List<BoxoDto>)
            : String {
        var stringJson = gson.toJson(list)
        return stringJson
    }

    // 2 Json -> List
    private fun getListFromJson(jsonString: String): List<BoxoDto>? {
        // 1 configura tipo di ritorno da json
        var listType = object : TypeToken<List<BoxoDto>>() {}.type

        // 2 trasforma json in lista
        var populist: List<BoxoDto>? = gson.fromJson(jsonString, listType) ?: listOf()

        println("XXX_BOXO_LOCAL_CONVERT_JSONTOLIST OK")
        return populist
    }


    // METODI SET /GET  OK

    // 2.1 salvo
    fun saveListInLocal(list: List<BoxoDto>) {

        // 1 conversione lista -> json
        val jsonString = getJson(list)

        // 2 aggiungi lista a DB
        boxoSharedPrefs.edit()
            .putString(BOXO_LIST_01, jsonString)
            .apply()
    }


    // 2.2 carico da prefs
    fun loadFromLocal(): List<BoxoDto> {

        // 1 estrazione stringa da DB
        var jsonString = boxoSharedPrefs.getString(BOXO_LIST_01, null)

        // 2 conversione stringa a lista
        var localList: List<BoxoDto>? = getListFromJson(jsonString ?: "")

        println("XXX_BOXO_LOCAL_LOAD LIST OK")

        return localList ?: listOf()

    }
}