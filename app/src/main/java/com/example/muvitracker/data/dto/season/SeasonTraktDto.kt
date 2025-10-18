package com.example.muvitracker.data.dto.season

import android.os.Parcelable
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class SeasonTraktDto(
    val number: Int?,
    val ids: Ids,
    val rating: Double?,
//    val votes: Int?, // not use
    @SerializedName("episode_count") val episodeCount: Int?, // tot planned
    @SerializedName("aired_episodes") val airedEpisodes: Int?, // tot released
    val title: String?, // "season 1"
    val overview: String?,
    @SerializedName("first_aired") val firstAired: String?,
//    @SerializedName("updated_at") val updatedAt: String?, // not use
    val network: String?
) : Parcelable {


    fun getYear(): String? {
        return firstAired?.substring(0, 4)
    }
}


fun SeasonTraktDto.toEntity(showId: Int): SeasonEntity {
    return SeasonEntity(
        seasonTraktId = ids.trakt,
        seasonNumber = number ?: 0,
        ids = ids,
        rating = rating?.firstDecimalApproxToString() ?: "0.0",
        episodeCount = episodeCount ?: 0,
        airedEpisodes = airedEpisodes ?: 0,
        title = title ?: "N/A",
        overview = overview ?: "N/A",
        releaseYear = getYear() ?: "N/A",
        network = network ?: "N/A",
        showId = showId // devo passarlo con la funzione
    )
}

// toDomain() : SeasonExtended {..} => on 'SeasonDao'


/*
     {
        "number": 0,
        "ids": {
            "trakt": 3962,
            "tvdb": 137481,
            "tmdb": 3627,
            "tvrage": null
        },
        "rating": 8.29189,
        "votes": 370,
        "episode_count": 286,
        "aired_episodes": 286,
        "title": "Specials",
        "overview": null,
        "first_aired": "2010-12-06T02:00:00.000Z",
        "updated_at": "2024-08-13T18:54:50.000Z",
        "network": "HBO"
    }
 */

