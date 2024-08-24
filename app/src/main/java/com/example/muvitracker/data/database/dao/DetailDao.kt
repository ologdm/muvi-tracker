package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailDao {

    // 1 - CREATE or UPDATE
    // (use always suspend)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertSingle(entity: DetailMovieEntity)


    // 2 - READ
    // (con flow) - osserva cambiamenti tabelle a cui fanno riferimento le query
    @Query("SELECT * FROM DetailMovieEntities WHERE traktId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<DetailMovieEntity?> // deve essere nullable

    @Query("SELECT * FROM DetailMovieEntities")
    fun readAllFlow(): Flow<List<DetailMovieEntity?>>


    // 3 - DELETE - nnot used
    @Delete
    fun deleteSingle(entity: DetailMovieEntity)


}