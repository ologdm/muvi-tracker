package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.DetailMovieEntityTmdb
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailMovieDaoTmdb {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(entity: DetailMovieEntityTmdb)

    @Delete
    fun deleteSingle(entity: DetailMovieEntityTmdb)

    // !! db deve poter restituire null
    @Query("SELECT * FROM detail_movie_entities_tmdb WHERE tmdbId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<DetailMovieEntityTmdb?>

    @Query("SELECT * FROM detail_movie_entities_tmdb")
    fun readAllFlow(): Flow<List<DetailMovieEntityTmdb>>
}