package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.database.entities.DetailShowEntity
import kotlinx.coroutines.flow.Flow

// 00 tutto
@Dao
interface DetailShowDao {

    // 1 - CREATE or UPDATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(entity: DetailShowEntity)


    // 2 - READ (con flow)
    @Query("SELECT * FROM DetailShowEntities WHERE traktId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<DetailShowEntity?> // deve essere nullable

    @Query("SELECT * FROM DetailShowEntities")
    fun readAllFlow(): Flow<List<DetailShowEntity?>>


    // 3 - DELETE - not used
    @Delete
    fun deleteSingle(entity: DetailShowEntity)


}