package com.example.muvitracker.data.dto.show.explore

import android.annotation.SuppressLint
import com.example.muvitracker.data.dto.show.ShowBaseDto
import com.example.muvitracker.domain.model.base.ShowBase
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AnticipatedShowDto(
    @SerialName("list_count") val listCount: Int,
    val show: ShowBaseDto
)


fun AnticipatedShowDto.toDomain(): ShowBase {
    return ShowBase(
        title = show.title,
        year = show.year,
        ids = show.ids
    )
}


/* JSON anticipated shows
[
  {
    "list_count": 14859,
    "show": {
      "title": "Agatha All Along",
      "year": 2024,
      "ids": {
        "trakt": 191186,
        "slug": "agatha-all-along",
        "tvdb": 412429,
        "imdb": "tt15571732",
        "tmdb": 138501,
        "tvrage": {}
      }
    }
  },
  ...
  ...
]
 */