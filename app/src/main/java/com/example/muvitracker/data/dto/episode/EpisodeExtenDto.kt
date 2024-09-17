package com.example.muvitracker.data.dto.episode

import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.dto.base.Ids
import com.google.gson.annotations.SerializedName

data class EpisodeExtenDto(
    val season: Int,
    val number: Int,
    val title: String,
    val ids: Ids,
    @SerializedName("number_abs") val numberAbs: Int, // posizione assoluta nel'intera serie
    val overview: String,
    val rating: Double,
//    val votes: Int,
//    @SerializedName("comment_count") val commentCount: Int,
    @SerializedName("first_aired") val firstAired: String,
//    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("available_translations") val availableTranslations: List<String>,
    val runtime: Int,
    @SerializedName("episode_type") val episodeType: String
) {
    fun getDateFromFirsAired(): String {
        "${firstAired.substring(0, 4)}/${firstAired.substring(5, 7)}/${firstAired.substring(8, 10)}"
        return "${firstAired.substring(8, 10)}/${firstAired.substring(5, 7)}/${
            firstAired.substring(
                0,
                4
            )
        }"
    }
}


// 00
fun EpisodeExtenDto.toEntity(showId: Int): EpisodeEntity {
    return EpisodeEntity(
        episodeTraktId = ids.trakt,
        seasonNumber = season,
        episodeNumber = number,
        title = title,
        ids = ids,
        numberAbs = numberAbs,
        overview = overview,
        rating = rating,
        firstAiredFormatted = getDateFromFirsAired(),
        availableTranslations = availableTranslations,
        runtime = runtime,
        episodeType = episodeType,
        showId = showId // passo con la funzione, seasonEntity ce l'ha
    )
}


/*
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

