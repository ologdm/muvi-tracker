package com.example.muvitracker.data.dto.movie

import com.example.muvitracker.domain.model.base.Movie


data class BoxofficeDtoM(
    val revenue: Int,
    val movie: MovieBaseDto
)


fun BoxofficeDtoM.toDomain(): Movie {
    return Movie(
        movie.title,
        movie.year,
        movie.ids
    )
}




//  JSON movies boxOffice
/* revenue + dto movie
[
  {
    "revenue": 48464322,
    "movie": {
      "title": "Hotel Transylvania 2",
      "year": 2015,
      "ids": {
        "trakt": 103449,
        "slug": "hotel-transylvania-2-2015",
        "imdb": "tt2510894",
        "tmdb": 159824
      }
    }
  },
  ....
  ....
]
 */




