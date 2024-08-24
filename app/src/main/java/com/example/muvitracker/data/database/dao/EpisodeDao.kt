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

    // 1. READ
    @Query("SELECT * FROM EpisodeEntities WHERE episodeTraktId=:episodeTraktId ")
    fun readSingleEpisodeById(episodeTraktId: Int): Flow<EpisodeEntity>
    // si potrebbe fare con filtro-> show, season, episode

    @Query("SELECT * FROM EpisodeEntities WHERE showId=:showId AND seasonNumber =:seasonNr")
    fun readAllEpisodesOfSeason(showId: Int, seasonNr: Int): Flow<List<EpisodeEntity>>


    // !!! check inserimento o update sempre uno alla volta
    // 2. INSERT
    @Insert
    suspend fun insertSingle(entity: EpisodeEntity)


    // 3.1 UPDATE dto
    @Update
    suspend fun updateDataSingleEpisode(entity: EpisodeEntity)


    // 3.2 UPDATE watched 00
    @Query(
        """
        UPDATE EpisodeEntities
        SET watched= NOT watched
        WHERE showId=:showId AND seasonNumber=:seasonNr AND episodeNumber=:episodeNr
        """
    )
    suspend fun updateWatchedSingleEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int,
    )




}


// NON USARE, LOGICA CHECK UNO AD UNO
//    @Insert
//    suspend fun insertMultiple(entities: List<EpisodeEntity>)

//    @Update
//    suspend fun updateDataAllEpisodes(entity: List<EpisodeEntity>)


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