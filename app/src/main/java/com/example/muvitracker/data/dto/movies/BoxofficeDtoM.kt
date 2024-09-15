package com.example.muvitracker.data.dto.movies

import com.example.muvitracker.data.database.entities.BoxoMovieEntity
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


fun BoxofficeDtoM.toEntity(): BoxoMovieEntity {
    return BoxoMovieEntity(
        traktId = movie.ids.trakt,
        title = movie.title,
        year = movie.year,
        ids = movie.ids
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




