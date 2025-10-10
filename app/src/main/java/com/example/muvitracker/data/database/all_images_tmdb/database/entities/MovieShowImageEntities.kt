package com.example.muvitracker.data.database.all_images_tmdb.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.dto.tmdb.old.MediaItem


@Entity(tableName = "movie_show_image_entities")
data class MovieShowImageEntity(
    @PrimaryKey val id: Int,

    @TypeConverters(ConvertersUtilsTmdb::class)
    val backdrops: List<MediaItem>, // 16/9 horizontal

    @TypeConverters(ConvertersUtilsTmdb::class)
    val posters: List<MediaItem>  // 2/3 vertical
)
