package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.data.database.entities.DetailShowEntityTmdb
import kotlinx.coroutines.flow.Flow

// OK
@Dao
interface DetailShowDaoTmdb {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(entity: DetailShowEntityTmdb)

    @Delete
    suspend fun deleteSingle(entity: DetailShowEntityTmdb)


    @Query("SELECT * FROM detail_show_entities_tmdb WHERE tmdbId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<DetailShowEntityTmdb?> // deve essere nullable

    @Query("SELECT * FROM detail_show_entities_tmdb")
    fun readAllFlow(): Flow<List<DetailShowEntityTmdb>>


}