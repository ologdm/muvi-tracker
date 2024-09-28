package com.example.muvitracker.data.dto.episode

import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.dto.base.Ids


data class EpisodeBaseDto(
    val season: Int,
    val number: Int,
    val title: String,
    val ids: Ids,
//    val show: ShowBaseDto // doesn't need
)

// minimal data to create a episode entity
fun EpisodeBaseDto.toPartialEntity(showId: Int): EpisodeEntity {
    return EpisodeEntity(
        episodeTraktId = ids.trakt,
        seasonNumber = season,
        episodeNumber = number,
        title = title,
        ids = ids,
        showId = showId
    )
}


//fun EpisodeBaseDto.toDomain(): Episode {
//    return Episode(
//        season = season,
//        number = number,
//        title = title,
//        ids = ids,
//        show = show.toDomain()
//    )
//}


/* https://api.trakt.tv/shows/game-of-thrones/seasons/1
[
    {
        "season": 1,
        "number": 1,
        "title": "Winter Is Coming",
        "ids": {
            "trakt": 73640,
            "tvdb": 3254641,
            "imdb": "tt1480055",
            "tmdb": 63056,
            "tvrage": null
        }
    },
    ...
    ...
]
 */