package com.example.muvitracker.data

import com.dropbox.android.external.store4.fresh
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.requests.ShowRequestKeys
import com.example.muvitracker.domain.model.DetailShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.singleOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsShowRepository @Inject constructor(
    database: MyDatabase
) {

    private val prefsShowDao = database.prefsShowDao()


    // for Prefs Frgment todo
//    override - old
//    fun getListFLow(): Flow<List<DetailShow>> {
//        val detailListFLow = detailShowRepository.getDetailListFlow()
//        val prefsListFLow = prefsShowDao.readAll()
//
//        return detailListFLow
//            .combine(prefsListFLow) { detailList, prefsList ->
//                // mappa valori non nulli
//                prefsList.mapNotNull { prefsEntityR ->
//                    val detailItem = detailList.find { detailEntityR ->
//                        detailEntityR?.traktId == prefsEntityR?.traktId
//                    }
//                    detailItem?.toDomain(prefsEntityR)
//                }.sortedByDescending {
//                    it.addedDateTime
//                }
//            }
//    }


    fun getListFLow(): Flow<List<DetailShow>> {
        // join: prefs + detail + watched episodes
        return prefsShowDao.getAllPrefsShows()
    }


    // LIKED OK
    // new or update
//    override
    suspend fun toggleLikedOnDB(id: Int) {
        // switch state on repository & update db
        val entity =
            prefsShowDao.readSingle(id) //  flow closing function
        if (entity != null) {
            prefsShowDao.toggleLiked(id)
        } else {
            prefsShowDao.insertSingle(
                PrefsShowEntity(
                    traktId = id,
                    liked = true,
                    addedDateTime = System.currentTimeMillis()
                )
            )
        }
    }


    // aggiungi elemento ai prefs se aggiungo watched ad un episodio
    suspend fun addWatchedToPrefs(showId: Int) {
        val prefsEntity = prefsShowDao.readSingle(showId)
        if (prefsEntity == null) {
            prefsShowDao.insertSingle(
                PrefsShowEntity(
                    traktId = showId,
                    liked = false,
                    addedDateTime = System.currentTimeMillis()
                )
            )
        }
    }



//    override
    suspend fun deleteItemOnDB(id: Int) {
        prefsShowDao.deleteSingle(id)
    }


}