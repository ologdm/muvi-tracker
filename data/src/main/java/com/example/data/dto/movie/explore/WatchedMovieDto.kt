package com.example.data.dto.movie.explore

import android.annotation.SuppressLint
import com.example.data.dto._support.toDomain
import com.example.data.dto.movie.MovieBaseDto
import com.example.domain.model.base.MovieBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class WatchedDtoM(
    @SerialName("watcher_count") val watcherCount: Int,
    @SerialName("play_count") val playCount: Int,
    @SerialName("collected_count") val collectedCount: Int,
    val movie: MovieBaseDto
)

fun WatchedDtoM.toDomain(): MovieBase {
    return MovieBase(
        title = movie.title,
        year = movie.year,
        ids = movie.ids.toDomain()
    )
}


/*
// JSON watched movie

[
    {
        "watcher_count": 10590,
        "play_count": 14121,
        "collected_count": 89,
        "movie": {
            "title": "The Hunt for Red October",
            "year": 1990,
            "ids": {
                "trakt": 1111,
                "slug": "the-hunt-for-red-october-1990",
                "imdb": "tt0099810",
                "tmdb": 1669
            }
        }
    }
    ....
    ....
]
 */
