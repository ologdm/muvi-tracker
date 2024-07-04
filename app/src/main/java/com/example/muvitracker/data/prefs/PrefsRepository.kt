package com.example.muvitracker.data.prefs

import androidx.lifecycle.LiveData
import com.example.muvitracker.data.detail.DetailLocalDS
import com.example.muvitracker.data.detail.toDomain
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.PrefsRepo
import com.example.muvitracker.utils.combineLatest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsRepository @Inject constructor(
    private val prefsLocalDS: PrefsLocalDS,
    private val detailLocalDS: DetailLocalDS
) : PrefsRepo {


    // GET ######################################################


    override fun getListFLow(): Flow<List<DetailMovie>> {
        TODO("Not yet implemented")
    }



    // SET ######################################################
    override fun toggleFavoriteOnDB(id: Int) {
        // switch state on db
        prefsLocalDS.toggleFavoriteOnDB(id) // bypass
    }


    override fun updateWatchedOnDB(id: Int, watched: Boolean) {
        // only update on db
        prefsLocalDS.updateWatchedOnDB(id, watched) // bypass
    }


    override fun deleteItemOnDB(movieId: Int) {
        prefsLocalDS.deleteItemFromDB(movieId) // bypass
    }


    //old
//    override fun getList(): LiveData<List<DetailMovie>> {
//        return combineLatest(
//            detailLocalDS.getLivedataList(),
//            prefsLocalDS.liveDataList
//        ) { detailList, prefsList ->
//            prefsList.mapNotNull { prefsItem ->
//                val detailItem = detailList.find { detailEntity ->
//                    detailEntity.ids.trakt == prefsItem.movieId
//                }
//                detailItem?.toDomain(prefsItem)
//            }
//        }
//    }

}
