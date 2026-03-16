package com.example.data.dto.movie.explore

import android.annotation.SuppressLint
import com.example.data.dto._support.toDomain
import com.example.data.dto.movie.MovieBaseDto
import com.example.domain.model.base.MovieBase
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BoxofficeDtoM(
    val revenue: Int,
    val movie: MovieBaseDto
)


fun BoxofficeDtoM.toDomain(): MovieBase {
    return MovieBase(
        movie.title,
        movie.year,
        movie.ids.toDomain()
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




