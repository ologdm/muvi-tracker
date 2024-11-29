package com.example.muvitracker.data.repositories.imagetmdb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.repositories.imagetmdb.database.entities.SeasonImageEntity
import kotlinx.coroutines.flow.Flow

// save - with id, + showId&seasonNr
// get - with showId&seasonNr

@Dao
interface SeasonImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleElement(element: SeasonImageEntity)


    @Query("SELECT * FROM season_image_entities WHERE tmdbShowId=:showId AND seasonNumber=:seasonNr")
    fun readSingle(showId: Int, seasonNr: Int): Flow<SeasonImageEntity>


}