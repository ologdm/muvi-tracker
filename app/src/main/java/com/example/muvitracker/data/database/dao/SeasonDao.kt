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

    @Query("SELECT * FROM season_table WHERE showId=:showId AND seasonNumber=:seasonNr")
    suspend fun readSingle(showId: Int, seasonNr: Int): SeasonEntity?

    @Query("SELECT * FROM season_table WHERE seasonTraktId=:seasonTraktId")
    suspend fun readSingleById(seasonTraktId: Int): SeasonEntity?

    @Insert
    suspend fun insertSingle(entity: SeasonEntity)

    @Update
    suspend fun updateSingle(entity: SeasonEntity)

    // not used
//    @Update
//    suspend fun updateAllByDto(entities: List<SeasonEntity>)


    @Query("SELECT COUNT(*) FROM season_table WHERE showId=:showId")
    suspend fun countAllSeasonsOfShow(showId: Int): Int


    // TODO 1.1.3 OK
//    creazione SeasonExtended:
//         SELECT - tutti i campi del output SeasonExtended,
//         ma devono avere lo stesso nome input putput, altrimenti AS nuovoNome
//         !! COALESCE (1,2) - sceglie il primo valore disponibile tra i due paramentri (1,2)
    @Transaction
    @Query(
        """
    SELECT
        -- trakt
        s.showId, -- traktShowId
        s.seasonNumber, 
        s.ids,
        s.traktRating, -- modificato 1.1.3
        s.episodeCount, 
        s.airedEpisodes, 
        -- tmdb or trakt
        s.title, 
        s.overview, 
        COALESCE(s.airDate, 0) AS releaseDate, -- modificato 1.1.3,
        s.network, 
        --COALESCE(SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END),0) AS watchedCount --1.1.3 fix COALESCE
        SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END) AS watchedCount --1.1.3 fix COALESCE
    FROM season_table AS s
    LEFT JOIN episode_table AS e ON s.seasonNumber = e.seasonNumber AND s.showId = e.showId
    WHERE s.showId = :showId
    GROUP BY s.seasonTraktId
"""
    )
    fun getAllSeasons(showId: Int): Flow<List<SeasonExtended>>


    // TODO 1.1.3 OK
    @Transaction
    @Query(
        """
    SELECT 
        s.showId,  -- traktShowId
        s.seasonNumber, 
        s.ids, 
        s.traktRating,  -- modificato 1.1.3
        s.episodeCount, 
        s.airedEpisodes, 
        
        s.title, 
        s.overview, 
        COALESCE(s.airDate, 0) AS releaseDate, -- modificato 1.1.3
        s.network, 
        -- COALESCE((CASE WHEN e.watched = 1 THEN 1 ELSE 0 END),0) AS watchedCount  --1.1.3 fix COALESCE
        SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END) AS watchedCount  --1.1.3 fix COALESCE
    FROM season_table AS s
    LEFT JOIN episode_table AS e ON s.seasonNumber = e.seasonNumber AND s.showId = e.showId
    WHERE s.showId = :showId AND s.seasonNumber = :seasonNumber
    GROUP BY s.seasonTraktId
"""
    )
    fun getSingleSeason(showId: Int, seasonNumber: Int): Flow<SeasonExtended?>

}


