package com.example.muvitracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.muvitracker.data.database.entities.PrefsEntityR
import kotlinx.coroutines.flow.Flow


@Dao
interface PrefsDao {


    // CREATE
    @Insert
    suspend fun insertSingle(entity: PrefsEntityR)


    // READ
    @Query("SELECT * FROM PrefsEntities WHERE traktId=:id")
    fun readSingle(id: Int): Flow<PrefsEntityR?>


    @Query("SELECT * FROM PrefsEntities")
    fun readAll(): Flow<List<PrefsEntityR?>>

    // UPDATE (personalizzati con query)
    @Query("UPDATE PrefsEntities SET liked = NOT liked WHERE traktId =:id")
    suspend fun updateLiked(id: Int)


    @Query("UPDATE PrefsEntities SET watched =:watched WHERE traktId =:id ")
    suspend fun updateWatched(id: Int, watched: Boolean)


    // DELETE
    @Query("DELETE FROM PrefsEntities WHERE traktId =:id")
    suspend fun deleteSingle(id: Int)


}