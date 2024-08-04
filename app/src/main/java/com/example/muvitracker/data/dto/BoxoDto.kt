package com.example.muvitracker.data.dto

import com.example.muvitracker.data.database.entities.BoxoMovieEntity
import com.example.muvitracker.data.dto.basedto.MovieDto
import com.example.muvitracker.domain.model.base.Movie


data class BoxoDto(
    val revenue: Int,
    val movie: MovieDto
)


// old
fun BoxoDto.toDomain(): Movie {
    return Movie(
        movie.title,
        movie.year,
        movie.ids
    )
}


// TODO
fun BoxoDto.toEntity(): BoxoMovieEntity {
    return BoxoMovieEntity(
        traktId = movie.ids.trakt,
        title = movie.title,
        year = movie.year,
        ids = movie.ids
    )
}




//  JSON BoxOffice #########################################################################
/* revenue + dto movie
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
 */




