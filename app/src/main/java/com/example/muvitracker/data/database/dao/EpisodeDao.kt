package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.muvitracker.data.database.entities.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    // to update by dto, check if element exist
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE episodeTraktId=:episodeTraktId 
        """
    )
    suspend fun readSingleById(episodeTraktId: Int): EpisodeEntity?


    // to episodeFragment
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr 
            AND episodeNumber =:episodeNr
        """
    )
    fun readSingle(showId: Int, seasonNr: Int, episodeNr: Int): Flow<EpisodeEntity>?


    // to seasonFragment
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber =:seasonNr"""
    )
    fun readAllOfSeason(showId: Int, seasonNr: Int): Flow<List<EpisodeEntity>>


    // to episode store
    @Insert
    suspend fun insertSingle(entity: EpisodeEntity)

    @Update
    suspend fun updateDataOfSingle(entity: EpisodeEntity)


    @Query(
        """
        UPDATE episode_entities
        SET watched = NOT watched
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr 
            AND episodeNumber=:episodeNr
            AND firstAiredFormatted IS NOT NULL
            AND firstAiredFormatted < DATETIME ('now')
        """
    )
    suspend fun toggleWatchedSingle(showId: Int, seasonNr: Int, episodeNr: Int)


    // 2. SEASON EPISODES - COUNT/ SET
    // to show, season fragment
    @Query(
        """
        SELECT COUNT(*)
        FROM episode_entities
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr 
            AND watched = 1
        """
    )
    suspend fun countSeasonWatchedEpisodes(showId: Int, seasonNr: Int): Int


    @Query(
        """
        UPDATE episode_entities
        SET watched=:watched
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr
            AND firstAiredFormatted IS NOT NULL
            AND firstAiredFormatted < DATETIME('now')
        """
    )
    suspend fun setSeasonWatchedAllEpisodes(showId: Int, seasonNr: Int, watched: Boolean)


    // 3. SHOW EPISODES -  COUNT/SET
    // to show fragment
    @Query(
        """
        SELECT COUNT (*)
        FROM episode_entities
        WHERE showId=:showId 
            AND watched = 1
    """
    )
    suspend fun countShowWatchedEpisodes(showId: Int): Int


    @Query(
        """
       UPDATE episode_entities
        SET watched=:watched
        WHERE showId=:showId
            AND firstAiredFormatted IS NOT NULL
            AND firstAiredFormatted < DATETIME('now')
    """
    )
    suspend fun setShowWatchedALlEpisodes(showId: Int, watched: Boolean)


    // count already download episodes of the show
    // serve solo per scaricare nuovi episodi mancanti (force download episodes)
    // it needs only in case -> to force download all the episodes
    @Query(
        """
        SELECT COUNT(*) FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber = :seasonNr 
        """
    )
    suspend fun countEpisodesOfSeason(showId: Int, seasonNr: Int): Int


}

