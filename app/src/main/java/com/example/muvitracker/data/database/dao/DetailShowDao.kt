package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.domain.model.DetailShow
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(entity: DetailShowEntity)

    @Query("SELECT * FROM detail_show_table WHERE traktId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<DetailShowEntity?> // deve essere nullable

    @Query("SELECT * FROM detail_show_table")
    fun readAllFlow(): Flow<List<DetailShowEntity>>

    @Delete
    fun deleteSingle(entity: DetailShowEntity)


    // TODO 1.1.3 OK
    // 1. devo prendere cambi necessari da -> Show e Prefs Entities
    // 21 show ent, 2 prefs, 1 ep
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
        
        COALESCE(SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END),0) AS watchedCount
    FROM 
        detail_show_table AS d   ---- nome tabella db
    LEFT JOIN 
        prefs_show_table AS p 
    ON 
        d.traktId = p.traktId
    LEFT JOIN 
        episode_table AS e 
    ON 
        d.traktId = e.showId
    WHERE 
        d.traktId = :showId
    GROUP BY 
        d.traktId
"""
    )
    fun getSingleFlow(showId: Int): Flow<DetailShow?>

}