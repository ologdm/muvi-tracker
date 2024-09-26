package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.domain.model.DetailShow
import kotlinx.coroutines.flow.Flow

// 00
@Dao
interface PrefsShowDao {

    // CREATE
    @Insert
    suspend fun insertSingle(entity: PrefsShowEntity)


    // READ
    @Query("SELECT * FROM prefs_show_entities WHERE traktId=:id")
    suspend fun readSingle(id: Int): PrefsShowEntity?


    @Query("SELECT * FROM prefs_show_entities")
    fun readAll(): Flow<List<PrefsShowEntity?>>


    // UPDATE (personalizzati con query) 00
    // (SET liked = NOT liked) -> toggle liked
    @Query("UPDATE prefs_show_entities SET liked = NOT liked WHERE traktId =:id")
    suspend fun toggleLiked(id: Int)


    // DELETE
    @Query("DELETE FROM prefs_show_entities WHERE traktId =:id")
    suspend fun deleteSingle(id: Int)



    // JOIN prefsEntity + detailEntity + (watchedEpisode) -> ottengo domain
    @Transaction
    @Query("""
    SELECT d.title, d.year, d.ids, d.tagline, d.overview, d.firstAired, d.runtime, 
           d.network, d.country, d.trailer, d.homepage, d.status, d.rating, d.votes, 
           d.language, d.languages, d.genres, d.airedEpisodes,
           p.liked, p.addedDateTime,
           SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END) AS watchedCount, 
           (SUM(CASE WHEN e.watched = 1 THEN 1 ELSE 0 END) = d.airedEpisodes) AS watchedAll
    FROM prefs_show_entities AS p
    LEFT JOIN detail_show_entities AS d ON p.traktId = d.traktId
    LEFT JOIN episode_entities AS e ON d.traktId = e.showId
    GROUP BY p.traktId
""")
    fun getAllPrefsShows(): Flow<List<DetailShow>>




}