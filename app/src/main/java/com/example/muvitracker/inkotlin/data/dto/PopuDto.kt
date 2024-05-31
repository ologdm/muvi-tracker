package com.example.muvitracker.inkotlin.data.dto

import com.example.muvitracker.inkotlin.data.dto.support.Ids
import com.example.muvitracker.inkotlin.domain.model.MovieModel


data class PopuDto(
    val title: String,
    val year: Int,
    val ids: Ids
)

fun PopuDto.toDomain(): MovieModel {
    return MovieModel(this.title, this.year, this.ids)
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






