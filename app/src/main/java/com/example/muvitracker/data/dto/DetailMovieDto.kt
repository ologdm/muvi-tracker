package com.example.muvitracker.data.dto

import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.dto.basedto.Ids
import java.io.StringReader


data class DetailMovieDto(
    val title: String,
    val year: Int,
    val ids: Ids,

// default value for the attributes that can be null
    val tagline: String = "",
    val overview: String = "",
    val released: String = "",
    val runtime: Int = 0,
    val country: String? = "",
//    @SerializedName("updated_at")
//    val updatedAt: String = "",
    val trailer: String = "", // TODO
    val homepage: String = "", // TODO
    val status: String = "", // TODO
    val rating: Float = 0F,
    val votes: Int = 0, // TODO
//    @SerializedName("comment_count")
//    val commentCount: Int = 0,
    val language: String = "", // TODO
//    val availableTranslations: List<String> = listOf(),
    val genres: List<String> = emptyList(),
//    val certification: String = ""
)


fun DetailMovieDto.toEntityR(): DetailMovieEntity {
    return DetailMovieEntity(
        traktId = ids.trakt,
        title = title,
        year = year,
        ids = ids,
        overview = overview,
        released = released,
        runtime = runtime,
        country = country ?: "",
        rating = rating,
        genres = genres
    )
}


/*
* JSON Details ####################################################

*  https://api.trakt.tv/movies/id
*  id = {tron-legacy-2010}?extended=full
{
    "title": "TRON: Legacy",
    "year": 2010,
    "ids": {
        "trakt": 343,
        "slug": "tron-legacy-2010",
        "imdb": "tt1104001",
        "tmdb": 20526
    },
    "tagline": "The Game Has Changed.",
    "overview": "Sam Flynn, the tech-savvy and daring son of Kevin Flynn, investigates his father's disappearance and is pulled into The Grid. With the help of  a mysterious program named Quorra, Sam quests to stop evil dictator Clu from crossing into the real world.",
    "released": "2010-12-16",
    "runtime": 125,
    "country": "us",
    "updated_at": "2014-07-23T03:21:46.000Z",
    "trailer": null,
    "homepage": "http://disney.go.com/tron/",
    "status": "released",
    "rating": 8,
    "votes": 111,
    "comment_count": 92,
    "language": "en",
    "available_translations": [
        "en"
    ],
    "genres": [
        "action"
    ],
    "certification": "PG-13"
}

 */
