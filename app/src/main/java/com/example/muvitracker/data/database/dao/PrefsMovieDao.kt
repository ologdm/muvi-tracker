package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.muvitracker.data.database.entities.PrefsMovieEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PrefsMovieDao {

    // CREATE
    @Insert
    suspend fun insertSingle(entity: PrefsMovieEntity)


    // READ
    @Query("SELECT * FROM prefs_movie_entities WHERE traktId=:id")
    fun readSingle(id: Int): Flow<PrefsMovieEntity?>

    @Query("SELECT * FROM prefs_movie_entities")
    fun readAll(): Flow<List<PrefsMovieEntity?>>


    // UPDATE (personalizzati con query)
    @Query("UPDATE prefs_movie_entities SET liked = NOT liked WHERE traktId =:id")
    suspend fun updateLiked(id: Int)

    @Query("UPDATE prefs_movie_entities SET watched =:watched WHERE traktId =:id ")
    suspend fun updateWatched(id: Int, watched: Boolean)


    // DELETE
    @Query("DELETE FROM prefs_movie_entities WHERE traktId =:id")
    suspend fun deleteSingle(id: Int)

}