package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.muvitracker.data.database.entities.PrefsMovieEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PrefsMovieDao {

    @Insert
    suspend fun insertSingle(entity: PrefsMovieEntity)

    @Query("SELECT * FROM prefs_movie_table WHERE traktId=:id")
    fun readSingle(id: Int): Flow<PrefsMovieEntity?>

    @Query("SELECT * FROM prefs_movie_table")
    fun readAll(): Flow<List<PrefsMovieEntity>>

    @Query("UPDATE prefs_movie_table SET liked = NOT liked WHERE traktId =:id")
    suspend fun updateLiked(id: Int)

    @Query("UPDATE prefs_movie_table SET watched =:watched WHERE traktId =:id ")
    suspend fun updateWatched(id: Int, watched: Boolean)

    @Query("DELETE FROM prefs_movie_table WHERE traktId =:id")
    suspend fun deleteSingle(id: Int)
}