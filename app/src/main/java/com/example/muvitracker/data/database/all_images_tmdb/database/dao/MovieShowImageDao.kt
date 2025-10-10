package com.example.muvitracker.data.database.all_images_tmdb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.MovieShowImageEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieShowImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleElement(element: MovieShowImageEntity)

    @Query("SELECT * FROM movie_show_image_entities WHERE id=:movieShowId")
    fun readSingle(movieShowId: Int): Flow<MovieShowImageEntity>

}