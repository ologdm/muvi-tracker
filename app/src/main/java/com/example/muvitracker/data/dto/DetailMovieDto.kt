package com.example.muvitracker.data.dto

import com.example.muvitracker.data.LanguageManager
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.dto.tmdb.DetailMovieDtoTmdb
import com.example.muvitracker.data.dto.tmdb.youtubeLinkTransformation
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.formatToSqliteCompatibleDate


// only base + rating

// traktDto - es deadpool - 190430
data class DetailMovieDto(
    // base
//    val title: String?, // Deadpool
    val year: Int?,  // 2016
    val ids: Ids,

    // extended
//    val tagline: String?, // Feel the love
//    val overview: String?, // The origin story of former Special Forces...
//    val released: String?, // 2016-02-12
//    val runtime: Int?, // 108
//    val country: String?, // us
//    @SerializedName("updated_at") val updatedAt: String?,
//    val trailer: String?, // https://youtube.com/watch?v=9vN6DHB6bJc
//    val homepage: String?, // http://www.20thcenturystudios.com/movies/deadpool
//    val status: String?, // released
    val rating: Float?,  // 8.25713 - trakt
//    val votes: Int?,     // 121407
//    @SerializedName("comment_count") val commentCount: Int?,
//    val language: String?, // "en" - lingua originale
//    val languages: List<String>?, //en,de - multilingue originali
//    val availableTranslations: List<String>?,
//    val genres: List<String>?, // action, thriller
//    val subgenres : List<String>?, // mercenary, based-on-comic, anti-hero
//    val certification: String?
//    val original_title : String // Deadpool
)


// Unione 2 dto utilizzate in un entity
fun mergeMoviesDtoToEntity(
    trakt: DetailMovieDto, tmdb: DetailMovieDtoTmdb
): DetailMovieEntity {
    return DetailMovieEntity(
        // trakt
        traktId = trakt.ids.trakt,
        year = trakt.year,
        ids = trakt.ids,
        // tmdb
        title = tmdb.title,
        tagline = tmdb.tagline,
        overview = tmdb.overview,
        status = tmdb.status,
        releaseDate = formatToSqliteCompatibleDate(tmdb.releaseDate),
        country = tmdb.originCountry ?: emptyList(),
        runtime = tmdb.runtime,
        originalLanguage = tmdb.originalLanguage,
        originalTitle = tmdb.originalTitle,
        genres = tmdb.genres?.map { it.name } ?: emptyList(), // entity not null
        youtubeTrailer = tmdb.videos?.youtubeLinkTransformation(), //https://youtube.com/watch?v=9vN6DHB6bJc
        homepage = tmdb.homepage,
        backdropPath = tmdb.backdropPath,
        posterPath = tmdb.posterPath,
        // voti
        traktRating = trakt.rating?.firstDecimalApproxToString(), // in 8.25713-> out 8.3
        tmdbRating = tmdb.voteAverage?.firstDecimalApproxToString(), // in 8.458 -> out 8.5
        // lingua
        currentTranslation = LanguageManager.getSystemLocaleTag()
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
