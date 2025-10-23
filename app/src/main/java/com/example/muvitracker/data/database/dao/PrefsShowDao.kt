package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.domain.model.DetailShow
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
        p.addedDateTime,
        ---COALESCE utile per default - prende primo valore non null tra quelli che gli passi
        COALESCE(SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END),0) AS watchedCount
    FROM prefs_show_table AS p
    LEFT JOIN detail_show_table AS d ON p.traktId = d.traktId
    LEFT JOIN episode_table AS e ON d.traktId = e.showId
    GROUP BY p.traktId
"""
    )
    fun getAllPrefs(): Flow<List<DetailShow>>

}