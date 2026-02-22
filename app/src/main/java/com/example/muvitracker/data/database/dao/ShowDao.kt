package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.muvitracker.data.database.entities.ShowEntity
import com.example.muvitracker.domain.model.Show
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(entity: ShowEntity)

    @Query("SELECT * FROM show_table WHERE traktId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<ShowEntity?> // deve essere nullable

    @Query("SELECT * FROM show_table")
    fun readAllFlow(): Flow<List<ShowEntity>>

    @Delete
    fun deleteSingle(entity: ShowEntity)


    // TODO 1.1.3 OK
    // show, prefs, episode, season table
    // 21 show ent, 3 prefs, 1 ep, 1 seas
    @Transaction
    @Query(
        """
    SELECT
        d.year,
        d.ids,
        d.airedEpisodes,
        
        d.title,
        d.tagline,
        d.overview,
        d.status,
        d.firstAirDate,
        d.lastAirDate,
        d.runtime,
        d.countries,
        d.originalLanguage,
        d.englishTitle,
        d.networks,
        d.genres,
        d.youtubeTrailer,
        d.homepage,
        d.traktRating,
        d.tmdbRating,
        d.imdbRating,
        d.rottenTomatoesRating,
        d.backdropPath,
        d.posterPath,
        d.currentTranslation,
    
        p.liked,
        COALESCE(p.notes, '') AS notes,
        p.addedDateTime,
    
        -- Subquery per contare episodi visti
        (SELECT COUNT(*)  FROM episode_table e WHERE e.showId = d.traktId AND e.watched = 1) AS watchedCount,
    
        -- Subquery per contare stagioni
        (SELECT COUNT(*) FROM season_table s WHERE s.showId = d.traktId) AS seasonsCount
    FROM
        show_table AS d
    LEFT JOIN
        prefs_show_table AS p
    ON
        d.traktId = p.traktId
    WHERE
        d.traktId = :showId
"""
    )
    fun getSingleFlow(showId: Int): Flow<Show?>

}