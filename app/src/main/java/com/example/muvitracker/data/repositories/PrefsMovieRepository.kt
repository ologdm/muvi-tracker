package com.example.muvitracker.data.repositories

import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.PrefsMovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.PrefsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefsMovieRepository @Inject constructor(
    database: MyDatabase,
    private val detailMovieRepository: DetailMovieRepository
) : PrefsRepo {

    private val prefsDao = database.prefsMovieDao()


    // GET
    override
    fun getListFLow(): Flow<List<DetailMovie>> {
        val detailListFLow = detailMovieRepository.getDetailListFlow()
        val prefsListFLow = prefsDao.readAll()

        return detailListFLow
            .combine(prefsListFLow) { detailList, prefsList ->
                // mappa valori non nulli
                prefsList.mapNotNull { prefsEntityR ->
                    val detailItem = detailList.find { detailEntityR ->
                        detailEntityR?.traktId == prefsEntityR?.traktId
                    }
                    detailItem?.toDomain(prefsEntityR)
                }.sortedByDescending {
                    it.addedDateTime
                }
            }
    }


    // SET
    override
    suspend fun toggleLikedOnDB(id: Int) {
        // switch state on repository & update db
        val entity =
            prefsDao.readSingle(id).firstOrNull() //  flow closing function
        if (entity != null) {
            prefsDao.updateLiked(id)
        } else {
            prefsDao.insertSingle(
                PrefsMovieEntity(
                    traktId = id,
                    liked = true,
                    watched = false,
                    addedDateTime = System.currentTimeMillis()
                )
            )
        }
    }


    override
    suspend fun updateWatchedOnDB(id: Int, watched: Boolean) {
        // only update db
        val entity = prefsDao.readSingle(id).firstOrNull()
        if (entity != null) {
            prefsDao.updateWatched(id, watched)
        } else {
            prefsDao.insertSingle(
                PrefsMovieEntity(
                    traktId = id,
                    liked = false,
                    watched = watched,
                    addedDateTime = System.currentTimeMillis()
                )
            )
        }
    }


    override
    suspend fun deleteItemOnDB(id: Int) {
        prefsDao.deleteSingle(id)
    }


}
