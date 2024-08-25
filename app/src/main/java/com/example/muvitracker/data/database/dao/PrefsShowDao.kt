package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import kotlinx.coroutines.flow.Flow

// 00
@Dao
interface PrefsShowDao {

    // CREATE
    @Insert
    suspend fun insertSingle(entity: PrefsShowEntity)


    // READ
    @Query("SELECT * FROM PrefsShowEntities WHERE traktId=:id")
    fun readSingle(id: Int): Flow<PrefsShowEntity?>

    @Query("SELECT * FROM PrefsShowEntities")
    fun readAll(): Flow<List<PrefsShowEntity?>>


    // UPDATE (personalizzati con query) 00
    // (SET liked = NOT liked) -> toggle liked
    @Query("UPDATE PrefsShowEntities SET liked = NOT liked WHERE traktId =:id")
    suspend fun toggleLiked(id: Int)


    // DELETE
    @Query("DELETE FROM PrefsEntities WHERE traktId =:id")
    suspend fun deleteSingle(id: Int)


    // WATCHED #############################################################
    // !!! aggiornato solo su season repository
    @Query(
        """
        UPDATE PrefsShowEntities 
        SET watchedAll=:watchedAll, watchedCount=:watchedCount
        WHERE traktId=:showId
"""
    )
    suspend fun updateWatched(showId: Int, watchedAll: Boolean, watchedCount: Int)




}