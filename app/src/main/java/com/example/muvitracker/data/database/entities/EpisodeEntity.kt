package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.episode.mergeEpisodeDtos
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.EpisodeExtended

//@Entity(tableName = "episode_entities")
//data class EpisodeEntity(
//    // base data - from base dto
//    @PrimaryKey val episodeTraktId: Int,
//    val seasonNumber: Int?,
//    val episodeNumber: Int?,
//    val title: String?,
//    val ids: Ids,
//    val showId: Int, // passed through parameter .toEntity(showId)
//    //
//    val numberAbs: Int?,
//    val overview: String?,
//    val rating: Double?,
//    val firstAiredFormatted: String?, // converted
//    val availableTranslations: List<String>?,
//    val runtime: Int?,
//    val episodeType: String?,
//    //save
//    val watched: Boolean = false
//)


// TODO: 1.1.3 OK
@Entity(tableName = "episode_table")
data class EpisodeEntity(
    @PrimaryKey val episodeTraktId: Int,
    val seasonNumber: Int, // -1 default
    val episodeNumber: Int, // -1 default
    val numberAbs: Int?,
    val ids: Ids,
    val showId: Int, // passed through parameter .toEntity(showId)
    //
    val title: String?, // tmdb traslated
    val overview: String?, // tmdb traslated
    val firstAiredFormatted: String?, // converted
    val runtime: Int?,
    val episodeType: String?,
    val traktRating: String?,

    val stillPath : String?, // only from tmdb

    //save
    val watched: Boolean = false
)


// 1.1.3 OK
// watched - unico dato salvato
fun EpisodeEntity.copyOnlyDtoData(watchedState: Boolean): EpisodeEntity {
    return this.copy(
        watched = watchedState
    )
}


// OK 1.1.3 OK
fun EpisodeEntity.toDomain(): EpisodeExtended {
    return EpisodeExtended(
        episodeTraktId = episodeTraktId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        numberAbs = numberAbs,
        ids = ids,
        showId = showId,

        title = title,
        overview = overview,
        firstAiredFormatted = firstAiredFormatted,
        runtime = runtime,
        episodeType = episodeType,
        traktRating = traktRating, // gia convertita 1.1.3

        watched = watched,
    )
}
