package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.basedto.Ids


@Entity(tableName = "EpisodeEntities")
data class EpisodeEntity(
    @PrimaryKey val episodeTraktId :Int,
    val seasonNumber: Int, // X
    val episodeNumber: Int, // X
    val title: String,
    val ids: Ids,
    val numberAbs: Int,
    val overview: String,
    val rating: Double,
    val firstAiredFormatted: String, // converted
    val availableTranslations: List<String>,
    val runtime: Int,
    val episodeType: String,

    val showId: Int, // X, passed through parameter .toEntity(showId)

    //save
    val watched: Boolean = false
)