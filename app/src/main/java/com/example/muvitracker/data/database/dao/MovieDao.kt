package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertSingle(entity: MovieEntity)
    @Delete
    fun deleteSingle(entity: MovieEntity)


//    @Query("SELECT * FROM detail_movie_entities WHERE traktId=:inputId")
//    fun readSingleFlow(inputId: Int): Flow<DetailMovieEntity?>
//
//    @Query("SELECT * FROM detail_movie_entities")
//    fun readAllFlow(): Flow<List<DetailMovieEntity>>

    @Query("SELECT * FROM movie_table WHERE traktId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<MovieEntity?>

    @Query("SELECT * FROM movie_table")
    fun readAllFlow(): Flow<List<MovieEntity>>

}