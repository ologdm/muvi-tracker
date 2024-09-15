package com.example.muvitracker.data.dto.season

import android.os.Parcelable
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.dto.basedto.Ids
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class SeasonExtenDto(
    val number: Int,
    val ids: Ids,
    val rating: Double,
//    val votes: Int, // not use
    @SerializedName("episode_count") val episodeCount: Int, // total usare
    @SerializedName("aired_episodes") val airedEpisodes: Int, // released
    val title: String, // "season 1"
    val overview: String?,
    @SerializedName("first_aired") val firstAired: String? = "",
//    @SerializedName("updated_at") val updatedAt: String, // not use
    val network: String
) : Parcelable {

    fun getYear(): String {
        return firstAired?.substring(0, 4) ?: ""
    }
}

// 00
fun SeasonExtenDto.toEntity(showId: Int): SeasonEntity {
    return SeasonEntity(
        seasonTraktId = ids.trakt,
        seasonNumber = number,
        ids = ids,
        rating = rating,
        episodeCount = episodeCount,
        airedEpisodes = airedEpisodes,
        title = title,
        overview = overview ?: "",
        releaseYear = getYear(),
        network = network,
        showId = showId // devo passarlo con la funzione
    )
}



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

