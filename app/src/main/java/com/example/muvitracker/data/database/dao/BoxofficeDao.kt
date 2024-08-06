package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.BoxoMovieEntity
import kotlinx.coroutines.flow.Flow

// room + paging

@Dao
interface BoxofficeDao {

    // insert or update
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<BoxoMovieEntity>)

    // read
    @Query("SELECT * FROM BoxofficeEntities")
    fun readAll() : Flow<List<BoxoMovieEntity>>

}

