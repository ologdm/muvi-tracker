package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.episode.EpisodeExtenDto
import com.example.muvitracker.data.dto.base.Ids

// completo - apertura season i
// parziale - detail, watchedAll

@Entity(tableName = "episode_entities")
data class EpisodeEntity(
    // base data - from base dto
    @PrimaryKey val episodeTraktId: Int,
    val seasonNumber: Int, // X
    val episodeNumber: Int, // X
    val title: String,
    val ids: Ids,
    val showId: Int, // X, passed through parameter .toEntity(showId)

    // todo - test salvataggio parziale
    val numberAbs: Int? = null,
    val overview: String? = null,
    val rating: Double? = null,
    val firstAiredFormatted: String? = null, // converted
    val availableTranslations: List<String>? = null,
    val runtime: Int? = null,
    val episodeType: String? = null,

    //save
    val watched: Boolean = false
)


// update only the dto part of entity
fun EpisodeEntity.copyDtoData(episodeDto: EpisodeExtenDto): EpisodeEntity {
    return this.copy(
        episodeTraktId = episodeDto.ids.trakt,
        seasonNumber = episodeDto.season ?: 0,
        episodeNumber = episodeDto.number ?: 0,
        title = episodeDto.title ?: "N/A",
        ids = episodeDto.ids,
        numberAbs = episodeDto.numberAbs ?: 0,
        overview = episodeDto.overview ?: "N/A",
        rating = episodeDto.rating ?: 0.0,
        firstAiredFormatted = episodeDto.getDateFromFirsAired() ?: "N/A",
        availableTranslations = episodeDto.availableTranslations ?: emptyList(),
        runtime = episodeDto.runtime ?: 0,
        episodeType = episodeDto.episodeType ?: "N/A",
        // showId already exist
        // watched remain the same
    )
}