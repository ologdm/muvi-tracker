package com.example.muvitracker.data.dto.movie

import com.example.muvitracker.domain.model.base.Movie
import com.google.gson.annotations.SerializedName

data class WatchedDtoM(
    @SerializedName("watcher_count") val watcherCount: Int,
    @SerializedName("play_count") val playCount: Int,
    @SerializedName("collected_count") val collectedCount: Int,
    val movie: MovieBaseDto
)

fun WatchedDtoM.toDomain(): Movie {
    return Movie(
        title = movie.title,
        year = movie.year,
        ids = movie.ids
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
