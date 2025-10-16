package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.episode.EpisodeExtenDto
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.EpisodeExtended
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.formatToSqliteCompatibleDate

@Entity(tableName = "episode_entities")
data class EpisodeEntity(
    // base data - from base dto
    @PrimaryKey val episodeTraktId: Int,
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val title: String?,
    val ids: Ids,
    val showId: Int, // passed through parameter .toEntity(showId)
    //
    val numberAbs: Int?,
    val overview: String?,
    val rating: Double?,
    val firstAiredFormatted: String?, // converted
    val availableTranslations: List<String>?,
    val runtime: Int?,
    val episodeType: String?,
    //save
    val watched: Boolean = false
)


// update only the dto part of entity
fun EpisodeEntity.copyDtoData(episodeDto: EpisodeExtenDto): EpisodeEntity {
    return this.copy(
        episodeTraktId = episodeDto.ids.trakt,
        seasonNumber = episodeDto.season,
        episodeNumber = episodeDto.number,
        title = episodeDto.title,
        ids = episodeDto.ids,
        numberAbs = episodeDto.numberAbs,
        overview = episodeDto.overview,
        rating = episodeDto.rating,
        firstAiredFormatted = formatToSqliteCompatibleDate(episodeDto.firstAired),
        availableTranslations = episodeDto.availableTranslations,
        runtime = episodeDto.runtime,
        episodeType = episodeDto.episodeType,
        // showId already exist
        // watched remain the same
    )
}


fun EpisodeEntity.toDomain(): EpisodeExtended {
    return EpisodeExtended(
        episodeTraktId = episodeTraktId,
        seasonNumber = seasonNumber ?: 0,
        episodeNumber = episodeNumber ?: 0,
        title = title ?: "N/A",
        ids = ids,
        showId = showId,
        numberAbs = numberAbs ?: 0,
        overview = overview ?: "N/A",
        rating = rating?.firstDecimalApproxToString() ?: "0.0",
        firstAiredFormatted = firstAiredFormatted,
        availableTranslations = availableTranslations ?: emptyList(),
        runtime = runtime ?: 0,
        episodeType = episodeType ?: "N/A",
        watched = watched,
    )
}
