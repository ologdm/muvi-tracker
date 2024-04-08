package com.example.muvitracker.inkotlin.mainactivity.popu.repo

import android.content.Context
import android.content.SharedPreferences
import com.example.muvitracker.inkotlin.repo.dto.PopuDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * ### SHARED PREFS MANAGMENT ###
 *
 *
 * id necessari shared prefs:
 *  1) "myPopuPrefs"   => id fase creazione prefs
 *  2) "chiaveLista01" => id fase get/set list in prefs
 *
 *
 * FUNZIONI:
 *
 * conversion: OK
 *  1) pr fun getJson
 *  2) pr fun getListFromJson
 *
 *
 * metodi get/set list: OK
 *  1) fun saveListInLocal
 *  2) fun loadFromLocal
 *
 *
 * null management: OK
 *             > loadFromLocal(), json null -> lista null
 *             > getListFromJson(), json null,
 *
 *
 */


// singleton OK
// attributi : context, gson, shared


class BaseLocalDS
private constructor(
    private val context: Context // vale come dichiarazione
) {

    private val gson = Gson()
    private val popuSharedPrefs: SharedPreferences =
        context.getSharedPreferences("myPopuPrefs", Context.MODE_PRIVATE)


    companion object {
        private var instance: BaseLocalDS? = null

        fun getInstance(context: Context): BaseLocalDS {
            if (instance == null) {
                instance = BaseLocalDS(context)
            }
            return instance!!
        }

        // costante
        private const val POPU_LIST_01 = "chiaveLista01"
    }


    // METODI CONVERSIONE OK

    // 1 List -> Json
    private fun getJson(list: List<PopuDto>)
            : String {
        var stringJson = gson.toJson(list)
        return stringJson
    }

    // 2 Json -> List
    private fun getListFromJson(jsonString: String): List<PopuDto>? {
        // 1 configura tipo di ritorno da json
        var listType = object : TypeToken<List<PopuDto>>() {}.type

        // 2 trasforma json in lista
        var populist: List<PopuDto>? = gson.fromJson(jsonString, listType) ?: listOf()

        println("XXX_POPU_LOCAL_CONVERT_JSONTOLIST OK")
        return populist
    }


    // METODI SET /GET  OK

    // 2.1 salvo
    fun saveListInLocal(list: List<PopuDto>) {

        // 1 conversione lista -> json
        val jsonString = getJson(list)

        // 2 aggiungi lista a DB
        popuSharedPrefs.edit()
            .putString(POPU_LIST_01, jsonString)
            .apply()
    }


    // 2.2 carico da prefs
    fun loadFromLocal(): List<PopuDto> {

        // 1 estrazione stringa da DB
        var jsonString = popuSharedPrefs.getString(POPU_LIST_01, null)

        // 2 conversione stringa a lista
        var localList: List<PopuDto>? = getListFromJson(jsonString ?: "")

        println("XXX_POPU_LOCAL_LOAD LIST OK")
        return localList ?: listOf()
        // se stringaDb null => lista null
        // null => elvis operator: jsonString + localList
    }
}