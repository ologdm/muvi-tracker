package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils


@Entity(tableName = "detail_movie_entities_tmdb")
data class DetailMovieEntityTmdb(
    @PrimaryKey val tmdbId: Int,
    val translation: String, // NOT NULL, la fornisco io
    // da dto, nullable
    val title: String?,
    val tagline: String?,
    val overview: String?,
    val status: String?, // tmdb non tradotta
    val voteTmdb: Double?,
    val trailerLink: String?, // convertito in link youtube
    @TypeConverters(ConvertersUtils::class) val genres: List<String>,
    val backdropPath: String?,
    val posterPath: String?,
)

