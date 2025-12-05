package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.domain.model.Show
import kotlinx.coroutines.flow.Flow

@Dao
interface PrefsShowDao {

//    @Upsert()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(entity: PrefsShowEntity)

    @Query("SELECT * FROM prefs_show_table WHERE traktId=:id")
    suspend fun readSingle(id: Int): PrefsShowEntity?

    @Query("SELECT * FROM prefs_show_table")
    fun readAll(): Flow<List<PrefsShowEntity>>

    @Query("UPDATE prefs_show_table SET liked = NOT liked WHERE traktId =:id")
    suspend fun toggleLiked(id: Int)

    @Query("DELETE FROM prefs_show_table WHERE traktId =:id")
    suspend fun deleteSingle(id: Int)


    // Get Domain -> with join
    // JOIN prefsEntity + detailEntity + (watchedEpisode)
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

        d.backdropPath,
        d.posterPath,
    
        d.currentTranslation,
    
        p.liked,
        COALESCE(p.notes, '') AS notes,
        p.addedDateTime,
    
        -- Subquery per contare episodi visti
        (SELECT COUNT(*) FROM episode_table e WHERE e.showId = d.traktId AND e.watched = 1) AS watchedCount,
    
        -- Subquery per contare stagioni
        (SELECT COUNT(*) FROM season_table s WHERE s.showId = d.traktId) AS seasonsCount
    FROM
        prefs_show_table AS p
    LEFT JOIN
        show_table AS d
    ON
        p.traktId = d.traktId
"""
    )
    fun getAllPrefs(): Flow<List<Show>>


    // 1.1.3 new
//    @Query("UPDATE prefs_show_table SET notes =:note WHERE traktId = :showId")
    @Query("UPDATE prefs_show_table SET notes =:notes WHERE traktId = :showId ")
    suspend fun setNotes (showId: Int, notes: String)



}