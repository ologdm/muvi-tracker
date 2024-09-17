package com.example.muvitracker.data.dto.season

import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.dto.base.Ids


data class SeasonBaseDto(
    val number: Int,
    val ids: Ids
)


// minimal data to create a episode entity
fun SeasonBaseDto.toPartialEntity(showId: Int): SeasonEntity {
    return SeasonEntity(
        seasonTraktId = ids.trakt,
        seasonNumber = number,
        ids = ids,
        showId = showId
    )
}


// dto fornito in https://api.trakt.tv/shows/game-of-thrones/seasons/
/*
[
    {
        "number": 0,
        "ids": {
            "trakt": 3962,
            "tvdb": 137481,
            "tmdb": 3627,
            "tvrage": null
        }
    },
    ...
    ...
]
 */