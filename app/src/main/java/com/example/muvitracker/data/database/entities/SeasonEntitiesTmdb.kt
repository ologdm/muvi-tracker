package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "season_entities_tmdb")
data class SeasonEntitiesTmdb(
    @PrimaryKey val seasonTmdbId: Int,
    val translation: String, // not null
    val seasonNumber: Int?,
    val name: String?,
    val overview: String?,
    val posterPath: String?,
    val voteAverage: Double?
)