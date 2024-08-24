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


@Dao
interface EpisodeDao {

    // 1. READ
    @Query("SELECT * FROM EpisodeEntities WHERE showId=:showId AND seasonNumber =:seasonNr")
    fun readSingleEpisodeOfSeason(showId: Int, seasonNr: Int): Flow<EpisodeEntity>

    @Query("SELECT * FROM EpisodeEntities WHERE showId=:showId AND seasonNumber =:seasonNr")
    fun readAllEpisodesOfSeason(showId: Int, seasonNr: Int): Flow<List<EpisodeEntity>>


    // 2. INSERT
    @Insert
    suspend fun insertSingle(entity: EpisodeEntity)

//    @Insert
//    suspend fun insertMultiple(entities: List<EpisodeEntity>)


    // 3.1 UPDATE dto
    @Update
    suspend fun updateSingleEpisodeDto(entity: EpisodeEntity)

//    @Update
//    suspend fun updateAllEpisodesDto(entity: List<EpisodeEntity>)


    // 3.2 UPDATE watched 00
    @Query(
        """
        UPDATE EpisodeEntities
        SET watched=:watched
        WHERE showId=:showId AND seasonNumber=:seasonNr AND episodeNumber=:episodeNr
        """
    )
    suspend fun updateWatchedSingleEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int,
        watched: Boolean,
    )




}


//@Query( non ha senso
//        """
//        UPDATE EpisodeEntities
//        SET watched=:watched
//        WHERE showId=:showId AND seasonNumber=:seasonNr
//        """
//    )
//   suspend fun updateWatchedAllEpisodes(
//        showId: Int,
//        seasonNr: Int,
//        watched: Boolean,
//    )