package com.example.muvitracker.data.prefs

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.muvitracker.data.detail.DetailLocalDS
import com.example.muvitracker.data.detail.toDomain
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.combineLatest
import javax.inject.Inject


class PrefsRepository @Inject constructor(
    private val prefsLocalDS: PrefsLocalDS,
    private val detailLocalDS: DetailLocalDS

) {

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


}
