//package com.example.muvitracker.data.prefs
//
//import android.content.Context
//import android.content.SharedPreferences
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//
//// ha tutte le funzioni
//// uguale a DetailLocalDS
//
//// ZZ tutto
//
//class PrefsLocalDSold(
//    val context: Context
//) {
//
//    private val gson = Gson()
//    private val sharedPreferences: SharedPreferences =
//        context.getSharedPreferences("prefs_cache", Context.MODE_PRIVATE)
//
//
//    // GET ######################################################  ZZ
//
//    // ZZ
//    fun getLivedataList()
//            : MutableLiveData<List<PrefsEntity>> {
//        val livedata = MutableLiveData<List<PrefsEntity>>()
//        livedata.value = loadSharedList() // carico in livedata
//
//        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
//            if (key == PREFS_KEY_01) {
////                livedata.value = loadSharedList() // onChange - aggiorno livedata
//                livedata.value = loadSharedList() // onChange - aggiorno livedata
//            }
//        }
//        return livedata
//    }
//
//
//
//    // ZZ
//    private fun loadSharedList(): List<PrefsEntity> {
//        val json =
//            sharedPreferences.getString(PREFS_KEY_01, "").orEmpty()
//        return getListFromJson(json)
//    }
//
//
//    // ZZ
//    private fun getListFromJson(jsonString: String): List<PrefsEntity> {
//        var listType = object : TypeToken<List<PrefsEntity>>() {}.type
//        return gson.fromJson(jsonString, listType) ?: emptyList()
//    }
//
//
//    // SET ###################################################### ZZ
//
//    // eu ZZ
//    fun toggleFavoriteOnDB(id: Int) {
//        val cache = loadSharedList().toMutableList()
//        val index = cache.indexOfFirst { it.movieId == id }
//        if (index != -1) {
//            val current = cache[index]
//            cache.set(index, current.copy(liked = !current.liked))
//        } else {
//            cache.add(PrefsEntity(liked = true, watched = false, movieId = id))
//        }
//        saveListInShared(cache)
//    }
//
//    // eu ZZ
//    fun updateWatchedOnDB(id: Int, watched: Boolean) {
//        val cache = loadSharedList().toMutableList()
//        val index = cache.indexOfFirst { it.movieId == id }
//        if (index != -1) {
//            val current = cache[index]
//            cache.set(index, current.copy(watched = watched))
//        } else {
//            cache.add(PrefsEntity(liked = false, watched = watched, movieId = id))
//        }
//        saveListInShared(cache)
//    }
//
//
//// ########################################################################### ZZ
//
//    fun saveListInShared(list: List<PrefsEntity>) {
//        sharedPreferences.edit()
//            .putString(PREFS_KEY_01, getJson(list))
//            .apply()
//    }
//
//
//    private fun getJson(list: List<PrefsEntity>): String {
//        return gson.toJson(list) ?: ""
//    }
//
//
//    // ###########################################################################
//    companion object {
//        private var instance: PrefsLocalDSold? = null
//
//        fun getInstance(context: Context): PrefsLocalDSold {
//            if (instance == null) {
//                instance = PrefsLocalDSold(context)
//            }
//            return instance!!
//        }
//
//        private const val PREFS_KEY_01 = "prefs_key_01"
//    }
//
//
//}
