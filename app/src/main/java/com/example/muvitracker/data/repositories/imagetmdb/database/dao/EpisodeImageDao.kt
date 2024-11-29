package com.example.muvitracker.data.repositories.imagetmdb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.repositories.imagetmdb.database.entities.EpisodeImageEntity
import kotlinx.coroutines.flow.Flow


// save - with id, + showId&seasonNr
// get - with showId & seasonNr & episodeNr

@Dao
interface EpisodeImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleElement(element: EpisodeImageEntity)


    @Query(
        """
        SELECT * 
        FROM episode_image_entities 
        WHERE tmdbShowId=:showId AND seasonNumber=:seasonNr AND episodeNumber =:episodeNr
        """
    )
    fun readSingle(showId: Int, seasonNr: Int, episodeNr: Int): Flow<EpisodeImageEntity>

}