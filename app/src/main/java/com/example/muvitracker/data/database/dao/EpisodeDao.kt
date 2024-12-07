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
            AND seasonNumber =:seasonNr
        """
    )
    fun readAllOfSeason(showId: Int, seasonNr: Int): Flow<List<EpisodeEntity>>


    // to episode store
    @Insert
    suspend fun insertSingle(entity: EpisodeEntity)

    @Update
    suspend fun updateDataOfSingle(entity: EpisodeEntity)


    // WATCHED ################################################
    // added constraint to set watched:
    //  1. firstAiredFormatted: not null,< now => only for last season
    //  2.


    // 1. SINGLE EPISODE
    @Query(
        """
        UPDATE episode_entities
        SET watched = NOT watched
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr 
            AND episodeNumber=:episodeNr
            AND (
            (seasonNumber = (
                SELECT MAX(seasonNumber)
                FROM episode_entities
                WHERE showId = :showId
            ) AND firstAiredFormatted < DATETIME('now'))
            OR seasonNumber != (
                SELECT MAX(seasonNumber)
                FROM episode_entities
                WHERE showId = :showId
            )
        )
        """
    )
    suspend fun toggleWatchedSingle(showId: Int, seasonNr: Int, episodeNr: Int)


    // 2. SEASON EPISODES
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


//    @Query(
//        """
//        UPDATE episode_entities
//        SET watched=:watched
//        WHERE showId=:showId
//            AND seasonNumber=:seasonNr
//            AND (
//                (
//                    seasonNumber = (
//                        SELECT MAX(seasonNumber)
//                        FROM episode_entities
//                        WHERE showId = :showId
//                    )
//                    AND firstAiredFormatted < DATETIME('now')
//                )
//                OR seasonNumber != (
//                    SELECT MAX(seasonNumber)
//                    FROM episode_entities
//                    WHERE showId = :showId
//                )
//            )
//        """
//    )
//    suspend fun setSeasonWatchedAllEpisodes(showId: Int, seasonNr: Int, watched: Boolean)

    @Query(
        """
        UPDATE episode_entities
        SET watched=:watched
        WHERE showId=:showId 
            AND seasonNumber=:seasonNr
            AND (
                CASE
                    WHEN seasonNumber = (SELECT MAX(seasonNumber) FROM episode_entities WHERE showId = :showId) 
                    THEN firstAiredFormatted < DATETIME('now')
                    ELSE 1
            END
            )
        """
    )
    suspend fun setSeasonWatchedAllEpisodes(showId: Int, seasonNr: Int, watched: Boolean)


    @Query(
        """
        SELECT COUNT(*) FROM episode_entities 
        WHERE showId=:showId 
            AND seasonNumber = :seasonNr 
        """
    )
    suspend fun countEpisodesOfSeason(showId: Int, seasonNr: Int): Int


}

