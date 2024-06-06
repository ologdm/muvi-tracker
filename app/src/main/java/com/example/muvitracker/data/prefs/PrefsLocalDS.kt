package com.example.muvitracker.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


// ha tutte le funzioni
// uguale a DetailLocalDS


class PrefsLocalDS(
    val context: Context
) {

    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("prefs_cache", Context.MODE_PRIVATE)


    // GET ######################################################  OK
    // ok
    fun getLivedataList()
            : LiveData<List<PrefsEntity>> {
        val livedata = MutableLiveData<List<PrefsEntity>>()
        livedata.value = getSharedList()
        // TODO aggiungere listener al cambiamento Shared
        return livedata
    }

    // ok
    private fun getSharedList()
            : List<PrefsEntity> {
        val json =
            sharedPreferences.getString(PREFS_KEY_01, "") ?: "" //?: per rendere String nonNull
        return getListFromJson(json)
    }

    // ok
    private fun getListFromJson(jsonString: String)
            : List<PrefsEntity> {
        var listType =
            object : TypeToken<List<PrefsEntity>>() {}.type
        var transformedList: List<PrefsEntity> = gson.fromJson(jsonString, listType) ?: emptyList()
        return transformedList
    }


    // SET ###################################################### OK

    // ok
    fun addOrSetItem(inputEntity: PrefsEntity) {
        val sharedList = getSharedList().toMutableList()

        val index = sharedList.indexOfFirst { sharedEntity ->
            inputEntity.movieId == sharedEntity.movieId
        }  // return index or -1

        if (index == -1) {
            sharedList.add(inputEntity)
        } else {
            sharedList[index] = inputEntity
        }

    }

    // ok
    fun saveListInSharedPreferences(list: List<PrefsEntity>) {
        sharedPreferences.edit()
            .putString(PREFS_KEY_01, getJson(list))
            .apply()
    }


    private fun getJson(list: List<PrefsEntity>): String {
        return gson.toJson(list) ?: ""
    }


    companion object {
        private var instance: PrefsLocalDS? = null

        fun getInstance(context: Context): PrefsLocalDS {
            if (instance == null) {
                instance = PrefsLocalDS(context)
            }
            return instance!!
        }

        private const val PREFS_KEY_01 = "prefs_key_01"
    }


}
