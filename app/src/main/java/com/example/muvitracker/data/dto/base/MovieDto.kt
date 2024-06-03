package com.example.muvitracker.data.dto.base

import com.example.muvitracker.domain.model.MovieItem

// Popular
data class MovieDto(
    val title: String,
    val year: Int,
    val ids: Ids
){
    fun imageUrl(): String = "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
}


fun MovieDto.toDomain(): MovieItem {
    return MovieItem(this.title, this.year, this.ids)
}






//  JSON Popular ##################################################

/* Popular (lista standard Movie)

  {
    "title": "The Dark Knight",
    "year": 2008,
    "ids": {
      "trakt": 16,
      "slug": "the-dark-knight-2008",
      "imdb": "tt0468569",
      "tmdb": 155
    }
  },
  */

// Returns the most popular movies.
// Popularity is calculated using the rating percentage and the number of ratings.






