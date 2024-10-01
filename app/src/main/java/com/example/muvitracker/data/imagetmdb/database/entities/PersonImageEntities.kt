package com.example.muvitracker.data.imagetmdb.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.imagetmdb.dto.MediaItem

// (id) == (tmdbId di trakt), fatto check


@Entity(tableName = "person_image_entities")
data class PersonImageEntity (
    @PrimaryKey val id: Int,

    @TypeConverters(ConvertersUtilsTmdb::class)
    val profiles: List<MediaItem> // dimensione - 2/3
)