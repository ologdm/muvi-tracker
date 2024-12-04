package com.example.muvitracker.data.repositories

import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.domain.model.DetailShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsShowRepository @Inject constructor(
    database: MyDatabase
) {

    private val prefsShowDao = database.prefsShowDao()


    //  override
    fun getListFLow(): Flow<List<DetailShow>> {
        // join: prefs + detail + watched episodes -> (caso tutto gia scaricato)
        return prefsShowDao.getAllPrefs()
            .map { shows ->
                shows.sortedByDescending { it.addedDateTime }
            }
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