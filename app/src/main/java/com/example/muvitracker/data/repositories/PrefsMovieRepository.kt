package com.example.muvitracker.data.repositories

import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.PrefsMovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.domain.repo.PrefsMovieRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefsMovieRepository @Inject constructor(
    database: MyDatabase,
    private val detailMovieRepositoryImpl: DetailMovieRepository
) : PrefsMovieRepo {

    private val prefsDao = database.prefsMovieDao()


    override fun getListFLow(): Flow<List<DetailMovie>> {
        val detailListFLow = detailMovieRepositoryImpl.getDetailListFlow()
        val prefsListFLow = prefsDao.readAll()

        return detailListFLow.combine(prefsListFLow) { detailList, prefsList ->
            prefsList.mapNotNull { prefsEntity ->
                val detailEntity = detailList.find { detailEntity ->
                    detailEntity?.traktId == prefsEntity.traktId
                }
                detailEntity?.toDomain(prefsEntity)
            }.sortedByDescending {
                it.addedDateTime
            }
        }
    }


    override suspend fun toggleLikedOnDB(movieId: Int) {
        val entity =
            prefsDao.readSingle(movieId).firstOrNull() //  flow closing function
        if (entity != null) {
            prefsDao.updateLiked(movieId)
        } else {
            prefsDao.insertSingle(
                PrefsMovieEntity(
                    traktId = movieId,
                    liked = true,
                    watched = false,
                    addedDateTime = System.currentTimeMillis()
                )
            )
        }
    }


    override suspend fun updateWatchedOnDB(movieId: Int, watched: Boolean) {
        val entity = prefsDao.readSingle(movieId).firstOrNull()
        if (entity != null) {
            prefsDao.updateWatched(movieId, watched)
        } else {
            prefsDao.insertSingle(
                PrefsMovieEntity(
                    traktId = movieId,
                    liked = false,
                    watched = watched,
                    addedDateTime = System.currentTimeMillis()
                )
            )
        }
    }


    override
    suspend fun deleteItemOnDB(movieId: Int) {
        prefsDao.deleteSingle(movieId)
    }


}
