//package com.example.muvitracker.data.database.all_images_tmdb.database.entities
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import androidx.room.TypeConverters
//import com.example.muvitracker.data.dto.tmdb.old.MediaItem
//
//@Entity(tableName = "episode_image_entities")
//data class EpisodeImageEntity (
//    @PrimaryKey val id: Int,
//
//    @TypeConverters(ConvertersUtilsImagesTmdb::class)
//    val stills: List<MediaItem>, // dimensione - hor 16/9
//
//    val tmdbShowId :Int,
//    val seasonNumber : Int,
//    val episodeNumber : Int
//)