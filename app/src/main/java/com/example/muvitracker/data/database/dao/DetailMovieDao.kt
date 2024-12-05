package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertSingle(entity: DetailMovieEntity)

    @Query("SELECT * FROM detail_movie_entities WHERE traktId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<DetailMovieEntity?>

    @Query("SELECT * FROM detail_movie_entities")
    fun readAllFlow(): Flow<List<DetailMovieEntity>>

    @Delete
    fun deleteSingle(entity: DetailMovieEntity)
}