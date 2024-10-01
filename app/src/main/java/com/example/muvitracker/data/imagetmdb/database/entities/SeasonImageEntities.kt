package com.example.muvitracker.data.imagetmdb.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.imagetmdb.dto.MediaItem

@Entity(tableName = "season_image_entities")
data class SeasonImageEntity(
    @PrimaryKey val id: Int,

    @TypeConverters(ConvertersUtilsTmdb::class)
    val posters: List<MediaItem>, // ratio 2/3

    val tmdbShowId :Int,
    val seasonNumber : Int
)