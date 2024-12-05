package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.domain.model.SeasonExtended
import kotlinx.coroutines.flow.Flow


@Dao
interface SeasonDao {

    @Query("SELECT * FROM season_entities WHERE showId=:showId AND seasonNumber=:seasonNr")
    suspend fun readSingle(showId: Int, seasonNr: Int): SeasonEntity?

    @Query("SELECT * FROM season_entities WHERE seasonTraktId=:seasonTraktId")
    suspend fun readSingleById(seasonTraktId: Int): SeasonEntity?

    @Insert
    suspend fun insertSingle(entity: SeasonEntity)

    @Update
    suspend fun updateSingleByDto(entity: SeasonEntity)

    @Update
    suspend fun updateAllByDto(entities: List<SeasonEntity>)

    @Query("SELECT COUNT(*) FROM season_entities WHERE showId=:showId")
    suspend fun countAllSeasonsOfShow(showId: Int): Int


    // entity -> to domain
    // join -> season + episodeEntity (watchedEpisode)
    //
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
    fun getAllSeasons(showId: Int): Flow<List<SeasonExtended>>


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
    fun getSingleSeason(showId: Int, seasonNumber: Int): Flow<SeasonExtended?>

}


