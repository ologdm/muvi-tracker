package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.basedto.Ids


//  00
@Entity(tableName = "SeasonEntities")
data class SeasonEntity(
    @PrimaryKey val seasonTraktId: Int,
    val seasonNumber: Int, // X
    val ids: Ids,
    val rating: Double, // converted
    val episodeCount: Int,
    val airedEpisodes: Int,
    val title: String,
    val overview: String,
    val releaseYear: String, // converted
    val network: String,

    val showId: Int, // X, passed through parameter .toEntity(showId)

    // save
    var watchedAll: Boolean = false, // TODO - permetterà di propagare gli watched true a tutte le serie
    var watchedCount: Int = 0
)