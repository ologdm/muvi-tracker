package com.example.muvitracker.data.dto.movies

import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.utils.dateFormatterInMMMyyy
import com.example.muvitracker.utils.firstDecimalApproxToString


data class DetailMovieDto(
    val title: String?,
    val year: Int?,
    val ids: Ids,
    // extended =full
    val tagline: String?,
    val overview: String?,
    val released: String?,
    val runtime: Int?,
    val country: String?,
//    @SerializedName("updated_at") val updatedAt: String?,
    val trailer: String?,
    val homepage: String?,
    val status: String?,
    val rating: Float?,
    val votes: Int?,
//    @SerializedName("comment_count") val commentCount: Int?,
    val language: String?,
//    val availableTranslations: List<String>?,
    val genres: List<String>?,
//    val certification: String?
)


fun DetailMovieDto.toEntity(): DetailMovieEntity {
    return DetailMovieEntity(
        traktId = ids.trakt,
        title = title ?: "N/A",
        year = year ?: 0,
        ids = ids,
        tagline = tagline ?: "N/A",
        overview = overview ?: "N/A",
        released = released?.dateFormatterInMMMyyy() ?: "N/A",
        runtime = runtime ?: 0,
        country = country ?: "N/A",
        trailer = trailer ?: "N/A",
        homepage = homepage ?: "N/A",
        status = status ?: "N/A",
        rating = rating?.firstDecimalApproxToString() ?: "0.0",
        votes = votes ?: 0,
        language = language ?: "N/A",
        genres = genres ?: emptyList()
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
