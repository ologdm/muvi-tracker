package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.muvitracker.data.database.entities.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    // to aggiornamento da dto, check esistenza elemento
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


    // to episode store update
    @Insert
    suspend fun insertSingle(entity: EpisodeEntity)

    // per episode store update
    @Update
    suspend fun updateDataOfSingle(entity: EpisodeEntity)


    // WATCHED
    // 1. CHECK/ TOGGLE WATCHED - SINGLE EPISODE
    // to seasonFragm, episode Fragm

    @Query(
        """
        UPDATE episode_entities
        SET watched = NOT watched
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr 
            AND episodeNumber=:episodeNr
        """
    )
    suspend fun toggleWatchedSingle(showId: Int, seasonNr: Int, episodeNr: Int)


    // 2. COUNT/ TOGGLE WATCHED - SEASON EPISODES
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
    fun countSeasonWatchedEpisodes(showId: Int, seasonNr: Int): Flow<Int>


    @Query(
        """
        UPDATE episode_entities
        SET watched=:watched
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr
        """
    )
    suspend fun toggleSeasonWatchedAllEpisodes(showId: Int, seasonNr: Int, watched: Boolean)


    // 3. COUNT/TOGGLE WATCHED - SHOW EPISODES
    //to show fragment
    @Query(
        """
        SELECT COUNT (*)
        FROM episode_entities
        WHERE showId=:showId 
            AND watched = 1
    """
    )
    fun countShowWatchedEpisodes(showId: Int): Flow<Int>


    @Query(
        """
       UPDATE episode_entities
        SET watched=:watched
        WHERE showId=:showId
    """
    )
    suspend fun toggleShowWatchedALlEpisodes(
        showId: Int, watched: Boolean
    )


    // CONTEGGIO AL MOMENTO DEGLI EPISODI GIA' SCARICATO
    // serve solo per scaricare nuovi episodi mancanti (force download episodes)
    @Query(
        """
        SELECT COUNT(*) FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber = :seasonNr 
        """
    )
    suspend fun countEpisodesOfSeason(showId: Int, seasonNr: Int): Int


}

