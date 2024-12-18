package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.BoxofficeMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxofficeDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<BoxofficeMovieEntity>)

    @Query("SELECT * FROM boxoffice_movie_entities")
    fun readAll() : Flow<List<BoxofficeMovieEntity>>

}




