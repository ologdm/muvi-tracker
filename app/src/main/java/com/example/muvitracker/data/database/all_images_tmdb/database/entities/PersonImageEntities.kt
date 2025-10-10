package com.example.muvitracker.data.database.all_images_tmdb.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.dto.tmdb.MediaItem

// (id) == (tmdbId di trakt), fatto check


@Entity(tableName = "person_image_entities")
data class PersonImageEntity (
    @PrimaryKey val id: Int,

    @TypeConverters(ConvertersUtilsTmdb::class)
    val profiles: List<MediaItem> // dimensione - 2/3
)