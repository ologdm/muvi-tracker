package com.example.muvitracker.data.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.data.dto.DetailDto
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


// conversione
// 1. getJson
// 2. getListFromJson

// save/load shared


class DetailLocalDS
private constructor(
    private val context: Context
) {

    // ATTRIBUTI
    val gson = Gson()
    val detaSharedPreferences: SharedPreferences =
        context.getSharedPreferences("myDetaList", Context.MODE_PRIVATE)

    private val localList = mutableListOf<DetailDto>()

    //    private val localList2 = mutableListOf<DetailItem>()
//    private val liveData = MutableLiveData<DetailDto>()


    companion object {
        private var instance: DetailLocalDS? = null
        fun getInstance(context: Context): DetailLocalDS {
            if (instance == null) {
                instance = DetailLocalDS(context)
            }
            return instance!!
        }

        private const val DETA_MOVIE_LIST_01 = "chiavelistaMovie_01"
    }


    // METODI CRUD
    // create, read, update, delete
    // 1. OK
    fun createItem(dto: DetailDto) {
        localList.add(dto)
        saveListInShared() // aggiorno locale
    }


    // 2. OK
    fun readItem(movieId: Int): DetailDto {
        var index = getItemIndex(movieId)
        loadListFromShared()
        return localList.get(index)
    }


    // 3.
    fun updateItem(dto: DetailDto) {
        var index = getItemIndex(dto)

        if (index != -1) {
            localList.set(index, dto)
            // copy ssu repo, dove vaod a cambiare stato
        }
        saveListInShared() // aggiorno locale
    }


    // 4. NON USARE
    private fun deleteItem(movieId: Int) {
        saveListInShared() // aggiorno locale
    }


    // METODI CHECK_ID: INDEX OK
    fun getItemIndex(inputDto: DetailDto): Int {
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
    fun getJson(list: List<DetailDto>): String {
        var jsonString = gson.toJson(list) ?: ""
        return jsonString
    }


    fun getListFromJson(jsonString: String): List<DetailDto> {
        // get il tipe token corretto
        var listType = object : TypeToken<List<DetailDto>>() {}.type
        // converti
        var transformedList: List<DetailDto> = gson.fromJson(jsonString, listType) ?: listOf()
        return transformedList
        // con gestione caso return null
    }


    // 2 Set/Get OK
    private fun saveListInShared() {
        var json: String = getJson(localList)

        detaSharedPreferences                    // DETA_MOVIE_LIST_01 - punto d'ingresso
            .edit()
            .putString(DETA_MOVIE_LIST_01, json)
            .apply()
    }


    fun loadListFromShared(): List<DetailDto> {
        // pesca json
        val json = detaSharedPreferences.getString(DETA_MOVIE_LIST_01, null) ?: ""
        val list = getListFromJson(json)         // converti json in lista
        localList.clear()
        localList.addAll(list)
        return localList.toList() // copia
    }

}