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


    fun getListFLow(): Flow<List<DetailShow>> {
        // (already downloaded case)
        // join on db: prefs + detail + watched episodes ->
        return prefsShowDao.getAllPrefs()
            .map { shows ->
                shows.sortedByDescending { it.addedDateTime }
            }
    }


    // LIKED CLICK
    // add new or update
    suspend fun toggleLikedOnDB(id: Int) {
        // switch state on repository & update db
        val entity =
            prefsShowDao.readSingle(id)
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

    // WATCHED CLICK
    // add prefs item, if I added watched on episode
    suspend fun checkAndAddIfWatchedToPrefs(showId: Int) {
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


    suspend fun deleteItemOnDB(id: Int) {
        prefsShowDao.deleteSingle(id)
    }


}