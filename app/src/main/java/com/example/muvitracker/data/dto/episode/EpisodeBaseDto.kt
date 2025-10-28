package com.example.muvitracker.data.dto.episode

import com.example.muvitracker.data.dto._support.Ids


data class EpisodeBaseDto(
    val season: Int,
    val number: Int,
    val title: String,
    val ids: Ids,
//    val show: ShowBaseDto // doesn't need
)



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