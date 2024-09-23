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
    // per aggiornamento da dto, check esistenza elemento || todo - fare true false
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE episodeTraktId=:episodeTraktId 
        """
    )
    suspend fun readSingleEpisodeById(episodeTraktId: Int): EpisodeEntity?


    // per episodeFragment
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr 
            AND episodeNumber =:episodeNr
        """
    )
    suspend fun readSingleEpisode(showId: Int, seasonNr: Int, episodeNr: Int): EpisodeEntity?


    // per seasonFragment
    @Query(
        """
        SELECT * FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber =:seasonNr""")
    fun readAllEpisodesOfSeason(showId: Int, seasonNr: Int): Flow<List<EpisodeEntity>>


    // INSERT /UPDATE from dto (without watched status)
    // per episode store update
    @Insert
    suspend fun insertSingle(entity: EpisodeEntity)

    // per episode store update
    @Update
    suspend fun updateDataSingleEpisode(entity: EpisodeEntity)


    // TODO WATCHED
    // 1. CHECK/ TOGGLE WATCHED - SINGLE EPISODE
    // seasonFragm, episode Fragm

//    @Query(
//        """
//        SELECT * FROM episode_entities
//        WHERE showId=:showId
//            AND seasonNumber=:seasonNr
//            AND episodeNumber=:episodeNr
//            AND watched= 1
//        """
//    )
//    suspend fun isEpisodeWatched(showId: Int, seasonNr: Int, episodeNr: Int): Boolean


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
    // for detail, season fragment
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
    // for show fragment
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
    """
    )
    suspend fun toggleShowAllWatchedEpisodes(
        showId: Int, watched: Boolean
    )


    // CONTEGGIO AL MOMENTO DEGLI EPISODI GIA' SCARICATO
    // serve solo per scaricare nuovi episodi mancanti (force push)
    @Query(
        """
        SELECT COUNT(*) FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber = :seasonNr 
        """
    )
    suspend fun countEpisodesBySeason(showId: Int, seasonNr: Int): Int


    @Query(
        """
        SELECT COUNT(*) FROM episode_entities 
        WHERE showId=:showId
        """
    )
    suspend fun countEpisodesByShow(showId: Int): Int


}


// TODO OOOO
// eliminare entrambe, sostituito da count


// boolean sql -> true=1, false=0

// todo
// (eugi) - peraz sql comode, select (tipo) - deve coincidere con return (tipo)
// e devono avere lo stesso nome...come nel esempio sotto
//@Query(
//    """
//        SELECT count(*) as number, count(*) as number2
//        FROM episode_entities
//        WHERE showId=:showId AND watched = 1
//    """
//)
//fun checkWatchedEpisodesOfShow(showId: Int): Flow<Test>
//
//data class Test(
//    val number: Int,
//    val number2: Int,
//)