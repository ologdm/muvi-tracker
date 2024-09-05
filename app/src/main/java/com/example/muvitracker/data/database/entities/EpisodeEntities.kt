package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.EpisodeExtenDto
import com.example.muvitracker.data.dto.basedto.Ids


@Entity(tableName = "EpisodeEntities")
data class EpisodeEntity(
    @PrimaryKey val episodeTraktId: Int,
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


// TODO fare ck se corretto !!
fun EpisodeEntity.copyDtoData(episodeDto: EpisodeExtenDto) : EpisodeEntity {
    return this.copy(
        episodeTraktId = episodeDto.ids.trakt,
        seasonNumber = episodeDto.season,
        episodeNumber = episodeDto.number,
        title = episodeDto.title,
        ids = episodeDto.ids,
        numberAbs = episodeDto.numberAbs,
        overview = episodeDto.overview,
        rating = episodeDto.rating,
        firstAiredFormatted = episodeDto.getDateFromFirsAired(),
        availableTranslations = episodeDto.availableTranslations,
        runtime = episodeDto.runtime,
        episodeType = episodeDto.episodeType,
        showId = showId,
        //  watched rimane stato db
    )
}