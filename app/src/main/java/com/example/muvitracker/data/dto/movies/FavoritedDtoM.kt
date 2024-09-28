package com.example.muvitracker.data.dto.movies

import com.example.muvitracker.domain.model.base.Movie
import com.google.gson.annotations.SerializedName

data class FavoritedDtoM(
    @SerializedName("user_count") val userCount: Int,
    val movie: MovieBaseDto
)


fun FavoritedDtoM.toDomain(): Movie {
    return Movie(
        title = movie.title,
        year = movie.year,
        ids = movie.ids
    )
}


/*
// JSON favorited movies
[
    {
        "user_count": 51,
        "movie": {
            "title": "Perspectiva",
            "year": 2024,
            "ids": {
                "trakt": 1074305,
                "slug": "perspectiva-2024",
                "imdb": "tt10127964",
                "tmdb": 1314236
            }
        }
    },
    ....
    ....
]
 */