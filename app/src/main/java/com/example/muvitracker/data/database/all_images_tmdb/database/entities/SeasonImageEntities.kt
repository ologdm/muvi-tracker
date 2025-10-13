package com.example.muvitracker.data.database.all_images_tmdb.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.dto.tmdb.old.MediaItem

@Entity(tableName = "season_image_entities")
data class SeasonImageEntity(
    @PrimaryKey val id: Int,

    @TypeConverters(ConvertersUtilsImagesTmdb::class)
    val posters: List<MediaItem>, // ratio 2/3

    val tmdbShowId :Int,
    val seasonNumber : Int
)