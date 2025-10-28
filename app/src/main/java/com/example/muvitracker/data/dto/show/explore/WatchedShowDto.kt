package com.example.muvitracker.data.dto.show.explore

import com.example.muvitracker.data.dto.show.ShowBaseDto
import com.example.muvitracker.domain.model.base.ShowBase
import com.google.gson.annotations.SerializedName

data class WatchedShowDto(
    @SerializedName("watcher_count") val watcherCount: Int,
    @SerializedName("play_count") val playCount: Int,
    @SerializedName("collected_count") val collectedCount: Int,
    @SerializedName("collector_count") val collectorCount: Int, // X- diff con movie
    val show: ShowBaseDto
)

fun WatchedShowDto.toDomain(): ShowBase {
    return ShowBase(
        title = show.title,
        year = show.year,
        ids = show.ids
    )
}


/*
// JSON watched shows

[
  {
    "watcher_count": 12313,
    "play_count": 69842,
    "collected_count": 10848,
    "collector_count": 456,
    "show": {
      "title": "The Lord of the Rings: The Rings of Power",
      "year": 2022,
      "ids": {
        "trakt": 150900,
        "slug": "the-lord-of-the-rings-the-rings-of-power",
        "tvdb": 367506,
        "imdb": "tt7631058",
        "tmdb": 84773,
        "tvrage": {}
      }
    }
  },
    ....
    ....
]
 */
