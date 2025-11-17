package com.example.muvitracker.data.repositories

import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.PrefsMovieEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.domain.repo.PrefsMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefsMovieRepositoryImpl @Inject constructor(
    database: MyDatabase,
    private val detailMovieRepository: DetailMovieRepository
) : PrefsMovieRepository {

    private val prefsMovieDao = database.prefsMovieDao()


    override fun getListFLow(): Flow<List<Movie>> {
        val detailListFLow = detailMovieRepository.getDetailListFlow()
        val prefsListFLow = prefsMovieDao.readAll()

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
            prefsMovieDao.readSingle(movieId).firstOrNull() //  flow closing function
        if (entity != null) {
            prefsMovieDao.updateLiked(movieId)
        } else {
            prefsMovieDao.insertSingle(
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
        val entity = prefsMovieDao.readSingle(movieId).firstOrNull()
        if (entity != null) {
            prefsMovieDao.updateWatched(movieId, watched)
        } else {
            prefsMovieDao.insertSingle(
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
        prefsMovieDao.deleteSingle(movieId)
    }


    override suspend fun setNotesOnDB(movieId: Int, notes: String) {
        val entity = prefsMovieDao.readSingle(movieId).firstOrNull()

        if (entity == null) {
            prefsMovieDao.insertSingle(
                PrefsMovieEntity(
                    traktId = movieId,
                    liked = false,
                    watched = false,
                    addedDateTime = System.currentTimeMillis(),
                    notes = notes
                )
            )
        } else {
            prefsMovieDao.setNotes(movieId, notes)
        }
    }


}
