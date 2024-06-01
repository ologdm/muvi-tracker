package com.example.muvitracker.inkotlin.data.dto

import com.example.muvitracker.inkotlin.data.dto.base.MovieDto
import com.example.muvitracker.inkotlin.domain.model.MovieItem


data class BoxoDto(
    val revenue: Int,
    val movie: MovieDto
)


fun BoxoDto.toDomain(): MovieItem {
    return MovieItem(this.movie.title, this.movie.year, this.movie.ids)
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

// Returns the top 10 grossing movies in the U.S. box office last weekend. (10 con piu incassi)
// Updated every Monday morning.



