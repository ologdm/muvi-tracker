package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.domain.model.SeasonExtended
import kotlinx.coroutines.flow.Flow

// @Update - richiede l'intera entity da aggiornare
// utilizzare episodi totali sempre

// TODO
//  1. read
//  2. insert
//  3.1 update from dto

@Dao
interface SeasonDao {

    // 1. READ 000
    @Query("SELECT * FROM season_entities WHERE showId=:showId AND seasonNumber=:seasonNr")
    suspend fun readSingleSeason(showId: Int, seasonNr: Int): SeasonEntity?

    @Query("SELECT * FROM season_entities WHERE seasonTraktId=:seasonTraktId")
    suspend fun readSingleSeasonById(seasonTraktId: Int): SeasonEntity?


    @Query("SELECT COUNT(*) FROM season_entities WHERE showId=:showId")
    suspend fun countAllSeasonsOfShow(showId: Int): Int


    // 2. INSERT 000
    // only new items
    @Insert
    suspend fun insertSingle(entity: SeasonEntity)


    // TODO modificare codice repository
    // 3.1 UPDATE dto
    @Update
    suspend fun updateSingleSeasonDto(entity: SeasonEntity)

    @Update
    suspend fun updateAllSeasonsDto(entities: List<SeasonEntity>)


    // JOIN season + episodeEntity (watchedEpisode) -> ottengo domain
    @Transaction
    @Query(
        """
    SELECT s.showId, s.seasonNumber, s.ids, s.rating, s.episodeCount, s.airedEpisodes, 
           s.title, s.overview, s.releaseYear, s.network, 
           SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END) AS watchedCount
    FROM season_entities AS s
    LEFT JOIN episode_entities AS e ON s.seasonNumber = e.seasonNumber AND s.showId = e.showId
    WHERE s.showId = :showId
    GROUP BY s.seasonTraktId
"""
    )
    fun getAllSeasonsExtended(showId: Int): Flow<List<SeasonExtended>>


    @Transaction
    @Query(
        """
    SELECT s.showId, s.seasonNumber, s.ids, s.rating, s.episodeCount, s.airedEpisodes, 
           s.title, s.overview, s.releaseYear, s.network, 
           SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END) AS watchedCount
    FROM season_entities AS s
    LEFT JOIN episode_entities AS e ON s.seasonNumber = e.seasonNumber AND s.showId = e.showId
    WHERE s.showId = :showId AND s.seasonNumber = :seasonNumber
    GROUP BY s.seasonTraktId
"""
    )
    fun getSingleSeasonExtended(showId: Int, seasonNumber: Int): Flow<SeasonExtended>

}


