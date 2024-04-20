package com.example.muvitracker.inkotlin.model.details

import android.content.Context
import android.content.SharedPreferences
import com.example.muvitracker.inkotlin.model.dto.DetaDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *  carico da locale in RAM solo all'apertura o creazione
 *  salvo ad ogni modifica CRUD
 *
 *  1° GET
 *  create, read, update, delete(no) OK
 *  getItemIndex() OK
 *
 *  saveListInShared(), OK
 *  loadListFromShared():return OK
 *
 */

// 1° step solo RAM
// 2° step sharedPrefs

// conversione
// 1. getJson
// 2. getListFromJson

// save/load shared


class DLocalDS
private constructor(
    private val context: Context
) {

    // ATTRIBUTI
    val gson = Gson()
    val detaSharedPreferences: SharedPreferences =
        context.getSharedPreferences("myDetaList", Context.MODE_PRIVATE)

    private val localList = mutableListOf<DetaDto>()


    companion object {
        private var instance: DLocalDS? = null

        fun getInstance(context: Context): DLocalDS {
            if (instance == null) {
                instance = DLocalDS(context)
            }
            return instance!!
        }

        // CONSTANTE
        private const val DETA_MOVIE_LIST_01 = "chiavelistaMovie_01"
        // TODO fare details shows
    }


    // METODI CRUD
    // create, read, update, delete


    // 1. OK
    fun createItem(dto: DetaDto) {
        localList.add(dto)
        println("XXX_DB_CREATE_ITEM")

        saveListInShared() // aggiorno locale
    }


    // 2. OK
    fun readItem(movieId: Int): DetaDto {
        var index = getItemIndex(movieId)

        loadListFromShared()
        println("XXX_DB_READ_ITEM")
        return localList.get(index)
    }


    // 3.
    fun updateItem(dto: DetaDto) {
        var index = getItemIndex(dto)

        if (index != -1) {
            localList.set(index, dto)
            // copy ssu repo, dove vaod a cambiare stato
        }
        println("XXX_DB_READ_ITEM")

        saveListInShared() // aggiorno locale
    }


    // 4. NON USARE
    private fun deleteItem(movieId: Int) {
        saveListInShared() // aggiorno locale
    }


    // METODI CHECK_ID: INDEX OK

    fun getItemIndex(inputDto: DetaDto): Int {
        var index = -1
        for (i in localList.indices) {
            val localDto = localList.get(i)
            if (localDto.ids.trakt == inputDto.ids.trakt) {
                index = i
                break // esci dal ciclo
            }
        }
        return index
    }

    fun getItemIndex(inputId: Int): Int {
        var index = -1
        for (i in localList.indices) {
            val localDto = localList.get(i)
            if (localDto.ids.trakt == inputId) {
                index = i
                break
            }
        }
        return index
    }


    // SHARED PREFERENCES

    // 1 Conversione

    // OK
    fun getJson(list: List<DetaDto>): String {
        var jsonString = gson.toJson(list) ?: ""
        return jsonString
    }

    // OK
    fun getListFromJson(jsonString: String): List<DetaDto> {
        // get il tipe token corretto
        var listType = object : TypeToken<List<DetaDto>>() {}.type
        // converti
        var transformedList: List<DetaDto> = gson.fromJson(jsonString, listType) ?: listOf()
        return transformedList
        // con gestione caso return null
    }


    // 2 Set/Get OK


    private fun saveListInShared() {
        var json: String = getJson(localList)

        println("XXX_DLOCAL_ getJson")

        // DETA_MOVIE_LIST_01 - punto d'ingresso
        detaSharedPreferences
            .edit()
            .putString(DETA_MOVIE_LIST_01, json)
            .apply()

        println("XXX_DLOCAL_ SAVE SHARED LIST")
    }


    fun loadListFromShared(): List<DetaDto> {
        // pesca json
        val json = detaSharedPreferences.getString(DETA_MOVIE_LIST_01, null) ?: ""

        // converti json in lista

        val list = getListFromJson(json)

        localList.clear()
        localList.addAll(list)

        return localList.toList() // copia
    }


}