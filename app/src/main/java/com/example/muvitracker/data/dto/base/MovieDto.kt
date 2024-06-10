package com.example.muvitracker.data.dto.base

import com.example.muvitracker.domain.model.base.Movie

// Popular
data class MovieDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun MovieDto.toDomain(): Movie {
    return Movie(this.title, this.year, this.ids)
}

// ###################################################################
// JSON Popular
 /* Popularity is calculated using the rating percentage and the number of ratings.
 {
    "title": "The Dark Knight",
    "year": 2008,
    "ids": {
      "trakt": 16,
      "slug": "the-dark-knight-2008",
      "imdb": "tt0468569",
      "tmdb": 155
    }
 }, */








