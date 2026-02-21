package com.example.muvitracker.data.dto

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class OmdbResultDto(
    val imdbID: String,

    val Ratings: List<OmdbRating>?, // imdb 7.1, r.tomatoes 83%, metacritic 68

//    val Metascore: String?,
    val imdbRating: String?, // 1.1.4 OK
//    val imdbVotes: String?,

    ) {

    // 1.1.4 OK
    val rottenTomatoesRating: String?
        get() {
            val rating = Ratings?.firstOrNull { it.Source == "Rotten Tomatoes" }?.Value
            return rating?.substringBefore("%")
        }

}


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class OmdbRating(
    val Source: String?,
    val Value: String?,
)


// tt5950044