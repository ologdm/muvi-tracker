package com.example.muvitracker.data.dto

import android.os.Parcelable
import com.example.muvitracker.data.dto.basedto.Ids
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class SeasonExtenDto(
    val number: Int,
    val ids: Ids,
    val rating: Double,
    val votes: Int,
    @SerializedName("episode_count") val episodeCount: Int,
    @SerializedName("aired_episodes") val airedEpisodes: Int,
    val title: String,
    val overview: String?,
    @SerializedName("first_aired") val firstAired: String,
    @SerializedName("updated_at") val updatedAt: String,
    val network: String
) :Parcelable {

    fun getYear(): String {
        return firstAired.substring(0,4)
    }
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

