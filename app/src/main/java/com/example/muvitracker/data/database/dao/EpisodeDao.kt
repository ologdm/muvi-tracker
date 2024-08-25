package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.muvitracker.data.database.entities.EpisodeEntity
import kotlinx.coroutines.flow.Flow

// TODO
//  1. read
//  2. insert
//  3. update -> logica parziale+totale da repository


// funzioni giuste
@Dao
interface EpisodeDao {

    // 1. READ ##################################################
    @Query("SELECT * FROM EpisodeEntities WHERE episodeTraktId=:episodeTraktId ")
    fun readSingleEpisodeById(episodeTraktId: Int): Flow<EpisodeEntity>
    // si potrebbe fare con filtro-> show, season, episode

    @Query("SELECT * FROM EpisodeEntities WHERE showId=:showId AND seasonNumber =:seasonNr")
    fun readAllEpisodesOfSeason(showId: Int, seasonNr: Int): Flow<List<EpisodeEntity>>


    // !!! check inserimento o update sempre uno alla volta
    // 2. INSERT ##################################################
    @Insert
    suspend fun insertSingle(entity: EpisodeEntity)


    // 3.1 UPDATE DTO ##################################################
    @Update
    suspend fun updateDataSingleEpisode(entity: EpisodeEntity)


    // 3.2 WATCHED TOGGLE #################################################
    // 1 toggle single
    @Query(
        """
        UPDATE EpisodeEntities
        SET watched = NOT watched
        WHERE showId=:showId AND seasonNumber=:seasonNr AND episodeNumber=:episodeNr
        """
    )
    suspend fun toggleWatchedSingleEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int,
    )

    // 2 toggle all
    @Query(
        """
        UPDATE EpisodeEntities
        SET watched=:watched
        WHERE showId=:showId AND seasonNumber=:seasonNr
        """
    )
    suspend fun toggleWatchedAllEpisodes(
        showId: Int,
        seasonNr: Int,
        watched: Boolean,
    )



    // 4 CHECK ####################################################
    // boolean sql -> true=1, false=0
    @Query("""
        SELECT * 
        FROM EpisodeEntities 
        WHERE showId=:showId AND seasonNumber=:seasonNr AND watched = 1
    """)
    fun checkWatchedEpisodesOfSeason(showId: Int, seasonNr: Int): Flow<List<EpisodeEntity>>


    @Query("""
        SELECT * 
        FROM EpisodeEntities 
        WHERE showId=:showId AND watched = 1
    """)
    fun checkWatchedEpisodesOfShow(showId: Int): Flow<List<EpisodeEntity>>



}

