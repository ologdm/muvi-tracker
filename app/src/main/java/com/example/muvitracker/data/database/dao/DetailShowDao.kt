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

    @Query("SELECT * FROM detail_show_entities WHERE traktId=:inputId")
    fun readSingleFlow(inputId: Int): Flow<DetailShowEntity?> // deve essere nullable

    @Query("SELECT * FROM detail_show_entities")
    fun readAllFlow(): Flow<List<DetailShowEntity>>

    @Delete
    fun deleteSingle(entity: DetailShowEntity)


    // Get Domain -> with join
    // detail + prefs + episodeCount
    @Transaction
    @Query(
        """
    SELECT 
        d.title, d.year, d.ids, d.tagline, d.overview, d.firstAired, d.runtime,
        d.network, d.country, d.trailer, d.homepage, d.status, d.rating, d.votes, 
        d.language, d.languages, d.genres, d.airedEpisodes, p.liked, p.addedDateTime,
        SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END) AS watchedCount
    FROM 
        detail_show_entities AS d
    LEFT JOIN 
        prefs_show_entities AS p 
    ON 
        d.traktId = p.traktId
    LEFT JOIN 
        episode_entities AS e 
    ON 
        d.traktId = e.showId
    WHERE 
        d.traktId = :showId
    GROUP BY 
        d.traktId
"""
    )
    fun getSingleFlow(showId: Int): Flow<DetailShow?>
    // campo where con campo select - devono coincidere!!!

}