package com.example.muvitracker.data.dto.episode

import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.formatToSqliteCompatibleDate
import com.google.gson.annotations.SerializedName

// trakt dto date (ISO 8601):        2015-09-21T17:30:00.000Z
// entity date (sqlite compatible):  2015-09-21 17:30:00

data class EpisodeTraktDto(
    val season: Int?,
    val number: Int?,
    val title: String?,
    val ids: Ids,
    @SerializedName("number_abs") val numberAbs: Int?, // posizione assoluta nel'intera serie
    val overview: String?,
    val rating: Double?,
//    val votes: Int?,
//    @SerializedName("comment_count") val commentCount: Int?,
    @SerializedName("first_aired") val firstAired: String?,
//    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("available_translations") val availableTranslations: List<String>?,
    val runtime: Int?,
    @SerializedName("episode_type") val episodeType: String?
)


// OK 1.1.3 OK
fun mergeEpisodeDtos(
    showId: Int,
    trakt: EpisodeTraktDto,
    tmdb: EpisodeTmdbDto?,
): EpisodeEntity {
    return EpisodeEntity(
        episodeTraktId = trakt.ids.trakt,
        seasonNumber = trakt.season ?: -1, // default = -1
        episodeNumber = trakt.number ?: -1, // default = -1
        numberAbs = trakt.numberAbs,
        ids = trakt.ids,
        showId = showId,
        //
        title = tmdb?.name ?: trakt.title, // traslate
        overview = tmdb?.overview ?: trakt.overview, // traslate
        firstAiredFormatted = formatToSqliteCompatibleDate(trakt.firstAired),
        runtime = trakt.runtime,
        episodeType = trakt.episodeType,
        traktRating = trakt.rating?.firstDecimalApproxToString(),
        stillPath = tmdb?.stillPath,
    )
}



/* TRAKT JSON
    {
        "season": 1,
        "number": 1,
        "title": "Winter Is Coming",
        "ids": {
            "trakt": 73640,
            "tvdb": 3254641,
            "imdb": "tt1480055",
            "tmdb": 63056,
            "tvrage": null
        },
        "number_abs": 1,
        "overview": "Jon Arryn, the Hand of the King, is dead. King Robert Baratheon plans to ask his oldest friend, Eddard Stark, to take Jon's place. Across the sea, Viserys Targaryen plans to wed his sister to a nomadic warlord in exchange for an army.",
        "rating": 8.08437,
        "votes": 14898,
        "comment_count": 38,
        "first_aired": "2011-04-18T01:00:00.000Z",
        "updated_at": "2024-08-18T13:53:51.000Z",
        "available_translations": [
            "ar",
            "bg",
            ....
            ....
            "zh"
        ],
        "runtime": 62,
        "episode_type": "series_premiere"
    }
 */

