package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.muvitracker.data.database.entities.EpisodeEntity
import kotlinx.coroutines.flow.Flow

// accoppio in base al'utilizzo


@Dao
interface EpisodeDao {

    // READ
    // to aggiornamento da dto, check esistenza elemento || todo - fare true false
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE episodeTraktId=:episodeTraktId 
        """
    )
    suspend fun readSingleEpisodeById(episodeTraktId: Int): EpisodeEntity?


    // to episodeFragment
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr 
            AND episodeNumber =:episodeNr
        """
    )
    suspend fun readSingleEpisode(showId: Int, seasonNr: Int, episodeNr: Int): EpisodeEntity?


    // to seasonFragment
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber =:seasonNr"""
    )
    fun readAllEpisodesOfSeason(showId: Int, seasonNr: Int): Flow<List<EpisodeEntity>>


    // INSERT /UPDATE from dto (without watched status)
    // to episode store update
    @Insert
    suspend fun insertSingle(entity: EpisodeEntity)

    // per episode store update
    @Update
    suspend fun updateDataSingleEpisode(entity: EpisodeEntity)


    // WATCHED
    // 1. CHECK/ TOGGLE WATCHED - SINGLE EPISODE
    // to seasonFragm, episode Fragm

    // check - read single

    @Query(
        """
        UPDATE episode_entities
        SET watched = NOT watched
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr 
            AND episodeNumber=:episodeNr
        """
    )
    suspend fun toggleWatchedSingleEpisode(showId: Int, seasonNr: Int, episodeNr: Int)


    // 2. COUNT/ TOGGLE WATCHED - SEASON EPISODES
    // to detail, season fragment
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
    suspend fun toggleSeasonAllWatchedAEpisodes(showId: Int, seasonNr: Int, watched: Boolean)


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
    suspend fun toggleShowAllWatchedEpisodes(
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
    suspend fun countEpisodesBySeason(showId: Int, seasonNr: Int): Int


}

