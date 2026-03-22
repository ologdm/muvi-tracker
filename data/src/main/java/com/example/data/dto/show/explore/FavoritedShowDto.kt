package com.example.data.dto.show.explore

import android.annotation.SuppressLint
import com.example.data.dto.show.ShowBaseDto
import com.example.domain.model.base.ShowBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class FavoritedShowDto(
    @SerialName("user_count") val userCount: Int,
    val show: ShowBaseDto
)


fun FavoritedShowDto.toDomain(): ShowBase {
    return ShowBase(
        title = show.title,
        year = show.year,
//        ids = show.ids.toDomain()
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