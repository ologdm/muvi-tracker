package com.example.muvitracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.DetailEntityR
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailDao {

    // 1 - CREATE or UPDATE
    // (use always suspend)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertSingle(entity: DetailEntityR)


    // 2 - READ
    // (con flow) - osserva cambiamenti tabelle a cui fanno riferimento le query
    @Query("SELECT * FROM DetailEntities WHERE traktId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<DetailEntityR?> // deve essere nullable

    @Query("SELECT * FROM DetailEntities")
    fun readAllFlow(): Flow<List<DetailEntityR?>>


    // 3 - DELETE
    @Delete
    fun deleteSingle(entity: DetailEntityR)


}