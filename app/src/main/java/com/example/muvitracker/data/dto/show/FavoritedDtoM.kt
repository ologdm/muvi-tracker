package com.example.muvitracker.data.dto.show

import com.example.muvitracker.domain.model.base.Show
import com.google.gson.annotations.SerializedName

data class FavoritedDtoS(
    @SerializedName("user_count") val userCount: Int,
    val show: ShowBaseDto
)


fun FavoritedDtoS.toDomain(): Show {
    return Show(
        title = show.title,
        year = show.year,
        ids = show.ids
    )
}


/*
// JSON favorited shows
[
  {
    "user_count": 70,
    "show": {
      "title": "Breaking Bad",
      "year": 2008,
      "ids": {
        "trakt": 1388,
        "slug": "breaking-bad",
        "tvdb": 81189,
        "imdb": "tt0903747",
        "tmdb": 1396,
        "tvrage": {}
      }
    }
  },
    ....
    ....
]
 */