package com.example.muvitracker.data.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/*
 * GET - con Livedata read automatico || update e delete non necessario
 * getLivedataList() : MUtableLiveData
 * readCacheList() : List<DetailEntity>
 * ListFromJson() - conversione
 *
 * SET - create MutableList, setItem
 * saveNewItemInSharedList () - check id, poi aggiungi alla lista esistente
 *
 * solo lettura, unica modifica sharedPrefs ->
 */


class DetailLocalDS
private constructor(
    private val context: Context
) {
    val gson = Gson()
    val detaSharedPreferences: SharedPreferences =
        context.getSharedPreferences("detail_cache", Context.MODE_PRIVATE)


// GET DATA ####################################################################

    fun getLivedataList(): MutableLiveData<List<DetailEntity>> {
        val liveData = MutableLiveData<List<DetailEntity>>()

        liveData.value = loadListFromShared() // first update

        detaSharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            // viene chiamata quando il valore (interno JSON) di qualsiasi "key" cambia
            if (key == DETAIL_CACHELIST_KEY_01) {
                liveData.value =
                    loadListFromShared() // update onChange JSON contenuto nella key rispettiva
            }
        }
        return liveData
    }


    private fun loadListFromShared(): List<DetailEntity> {
        val json = detaSharedPreferences.getString(DETAIL_CACHELIST_KEY_01, null) ?: ""
        return getListFromJson(json) // conversione
    }


    private fun getListFromJson(jsonString: String): List<DetailEntity> {
        var listType =
            object : TypeToken<List<DetailEntity>>() {}.type          // get il tipe token corretto
        var transformedList: List<DetailEntity> =
            gson.fromJson(jsonString, listType) ?: listOf()        // converti
        return transformedList
        // con gestione caso return null
    }


// SET ####################################################################

    fun saveNewItemInSharedList(item: DetailEntity) {
        // TODO checkID, serve??

        //prendere lista attuale
        val updatedList = loadListFromShared().toMutableList()
        updatedList.add(item)

        // update shared list
        detaSharedPreferences.edit()
            .putString(DETAIL_CACHELIST_KEY_01, getJson(updatedList))
            .apply()
    }


    private fun getJson(list: List<DetailEntity>): String {
        var jsonString = gson.toJson(list) ?: ""
        return jsonString
    }


// METODI CHECK_ID: INDEX OK ####################################################################

    // return index o -1
    private fun getItemIndex(inputItem: DetailEntity): Int {
        val list = loadListFromShared()

        var index = -1
        for (i in list.indices) {
            val cachedItem = list.get(i)
            if (cachedItem.ids.trakt == inputItem.ids.trakt) {
                index = i
                break
            }
        }
        return index
    }


    fun getItemIndex(inputId: Int): Int {
        val list = loadListFromShared()

        var index = -1
        for (i in list.indices) {
            val cachedItem = list.get(i)
            if (cachedItem.ids.trakt == inputId) {
                index = i
                break
            }
        }
        return index
    }


    // ####################################################################
    companion object {
        private var instance: DetailLocalDS? = null
        fun getInstance(context: Context): DetailLocalDS {
            if (instance == null) {
                instance = DetailLocalDS(context)
            }
            return instance!!
        }

        private const val DETAIL_CACHELIST_KEY_01 = "detail_cachelist_key_01"
    }
}


// NON SERVONO

//    fun readItem(movieId: Int): DetailDto {
//        var index = getItemIndex(movieId)
//        loadListFromShared()
//        return localList.get(index)
//    }
//
// TODO
//    fun updateItem(dto: DetailDto) {
//        var index = getItemIndex(dto)
//
//        if (index != -1) {
//            localList.set(index, dto)
//            // copy ssu repo, dove vaod a cambiare stato
//        }
//        saveListInShared() // aggiorno locale
//    }
//

//    private fun deleteItem(movieId: Int) {
//        saveListInShared() // aggiorno locale
//    }

//##############################################################

// METODI CHECK_ID: INDEX OK
//private fun getItemIndex(inputDto: DetailDto): Int {
//    var index = -1
//
//    for (i in localList.indices) {
//        val localDto = localList.get(i)
//        if (localDto.ids.trakt == inputDto.ids.trakt) {
//            index = i
//            break // esci dal ciclo
//        }
//    }
//    return index
//}
//
//
//fun getItemIndex(inputId: Int): Int {
//    var index = -1
//
//    for (i in localList.indices) {
//        val localDto = localList.get(i)
//        if (localDto.ids.trakt == inputId) {
//            index = i
//            break
//        }
//    }
//    return index
//}