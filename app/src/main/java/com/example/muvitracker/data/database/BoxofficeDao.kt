package com.example.muvitracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.PopularMovieEntity
import kotlinx.coroutines.flow.Flow

// room + paging
// == a popular

@Dao
interface BoxofficeDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<PopularMovieEntity>)

    @Query("SELECT * FROM PopularEntities")
    fun readAll() : Flow<List<PopularMovieEntity>>
}