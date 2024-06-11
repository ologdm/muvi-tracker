package com.example.muvitracker.data.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailLocalDS
private constructor(
    private val context: Context
) {
    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("detail_cache", Context.MODE_PRIVATE)


// GET ####################################################################

    fun getLivedataList(): MutableLiveData<List<DetailEntity>> {
        val liveData = MutableLiveData<List<DetailEntity>>()
        liveData.value = loadSharedList() // first update

        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            // called when the (internal JSON) value of any "key" changes
            if (key == DETAIL_KEY_01) {
                liveData.value = loadSharedList() // update onChange JSON content in its key
            }
        }
        return liveData
    }

    private fun loadSharedList(): List<DetailEntity> {
        val json = sharedPreferences.getString(DETAIL_KEY_01, null) ?: ""
        return getListFromJson(json)
    }

    private fun getListFromJson(jsonString: String): List<DetailEntity> {
        val listType = object : TypeToken<List<DetailEntity>>() {}.type
        return gson.fromJson(jsonString, listType) ?: listOf()
    }


// SET ####################################################################

    fun addOrUpdateItem(inputEntity: DetailEntity) {
        val currentSharedList = loadSharedList().toMutableList()
        val index = currentSharedList.indexOfFirst { sharedEntity ->
            sharedEntity.ids.trakt == inputEntity.ids.trakt
        } // return condition index or -1

        if (index == -1) {
            currentSharedList.add(inputEntity)
        } else {
            currentSharedList[index] = inputEntity
        }
        saveListInSharedPreferences(currentSharedList)
    }


    private fun saveListInSharedPreferences(updatedList: List<DetailEntity>) {
        // update shared list
        sharedPreferences
            .edit()
            .putString(DETAIL_KEY_01, getJson(updatedList))
            .apply()
    }


    private fun getJson(list: List<DetailEntity>): String {
        return gson.toJson(list) ?: ""
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

        private const val DETAIL_KEY_01 = "detail_key_01"
    }
}


