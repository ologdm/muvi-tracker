package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils


@Entity(tableName = "detail_show_entities_tmdb")
data class DetailShowEntityTmdb(
    @PrimaryKey val tmdbId: Int,
    val translation: String, // NOT NULL, la fornisco io
    // da dto, nullable
    val title: String?, // titolo serie
    val tagline : String?,
    val overview: String?,
    val voteTmdb: Double?, // popularity
    val trailerLink: String?, // convertito in link youtube
    @TypeConverters(ConvertersUtils::class) val genres: List<String>,
    val backdropPath: String?,
    val posterPath: String?,
)