package com.example.muvitracker.data.prefs

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.muvitracker.data.detail.DetailLocalDS
import com.example.muvitracker.data.detail.toDomain
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.combineLatest


class PrefsRepository(
    private val context: Context
) {
    private val prefsLocalDS = PrefsLocalDS.getInstance(context)
    private val detailLocalDS = DetailLocalDS.getInstance(context)


    // GET ######################################################

    fun getList(): LiveData<List<DetailMovie>> {
        return combineLatest(
            detailLocalDS.getLivedataList(),
            prefsLocalDS.liveDataList
        ) { detailList, prefsList ->
            prefsList.mapNotNull { prefsItem ->
                val detailItem = detailList.find { detailEntity ->
                    detailEntity.ids.trakt == prefsItem.movieId
                }
                detailItem?.toDomain(prefsItem)
            }
        }
    }


    // SET ######################################################
    fun toggleFavoriteOnDB(id: Int) {
        // switch state on db
        prefsLocalDS.toggleFavoriteOnDB(id) // bypass
    }


    fun updateWatchedOnDB(id: Int, watched: Boolean) {
        // only update on db
        prefsLocalDS.updateWatchedOnDB(id, watched) // bybass
    }


    fun deleteItemOnDB(movieId: Int) {
        prefsLocalDS.deleteItemFromDB(movieId) // bypass
    }




    // ######################################################
    companion object {
        private var instance: PrefsRepository? = null
        fun getInstance(context: Context): PrefsRepository {
            if (instance == null) {
                instance = PrefsRepository(context)
            }
            return instance!!
        }
    }
}
