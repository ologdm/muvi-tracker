package com.example.muvitracker.data.dto.show.detail

import com.example.muvitracker.data.LanguageManager
import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.data.dto.movie.detail.youtubeLinkTransformation
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.google.gson.annotations.SerializedName

// only base + rating


// trakt dto - game of thrones - 1390 - en
data class DetailShowTraktDto(
    val title: String?,
    val year: Int?, // 2011 - first year
    val ids: Ids, // + tvdb

//    val tagline: String?, // Winter is coming
//    val overview: String?, // Seven noble families fight.....
//    @SerializedName("first_aired") val firstAired: String?, // "2011-04-18T01:00:00.000Z"
//    val airs : Airs // [ day, time, timezone ]
    val runtime: Int?, // 55
//    val certification: String,
//    val network: String?, // HBO
//    val country: String?, // us
//    @SerializedName("updated_at") val updatedAt: String = "", // date
    val trailer: String?, // https://youtube.com/watch?v=KPLWWIOCOOQ
//    val homepage: String?, // http://www.hbo.com/game-of-thrones
//    val status: String?, // ended
    val rating: Float?, // 8.89378
//    val votes: Int?, // 142022
//    @SerializedName("comment_count") val commentCount: Int = 0, // 444
//    val language: String?, // en
//    val languages: List<String>?, // [en, it]
//    val availableTranslations: List<String> = emptyList(), // [en, it, tr, ...]
//    val genres: List<String>?, // [drama, fantasy]
    @SerializedName("aired_episodes") val airedEpisodes: Int? // 733
)


fun mergeShowsDtoToEntity(
    trakt: DetailShowTraktDto, tmdb: DetailShowTmdbDto
): DetailShowEntity {
    return DetailShowEntity(
        // trakt ok
        traktId = trakt.ids.trakt,
        year = trakt.year,
        ids = trakt.ids,
        airedEpisodes = trakt.airedEpisodes ?: 0, // logica veccchia, serve per calcolo

        // tmdb
        title = tmdb.name,
        tagline = tmdb.tagline,
        overview = tmdb.overview,
        status = tmdb.status,
        firstAirDate = tmdb.firstAirDate, // test con tmdb
        lastAirDate = tmdb.lastAirDate, // TODO inserire a ui : inizio -> fine
        runtime = trakt.runtime,
        countries = tmdb.originCountry ?: emptyList(), // entity not null
        originalLanguage = tmdb.originalLanguage,
        languages = tmdb.languages ?: emptyList(), // entity not null
        originalTitle = tmdb.originalName,
        networks = tmdb.networks?.map { it.name } ?: emptyList(), // entity not null
        genres = tmdb.genres?.map { it.name } ?: emptyList(), // entity not null
        //
        youtubeTrailer = tmdb.videos?.youtubeLinkTransformation() ?: trakt.trailer,
        homepage = tmdb.homepage,
        //
        backdropPath = tmdb.backdropPath,
        posterPath = tmdb.posterPath,
        // ratings
        tmdbRating = tmdb.voteAverage?.firstDecimalApproxToString(), // in 8.458 -> out 8.5
        traktRating = trakt.rating?.firstDecimalApproxToString(),

        // sistema
        currentTranslation = LanguageManager.getSystemLocaleTag()
    )
}


/*
When getting full extended info, the status field can have a value of
- returning series (airing right now),
- continuing (airing right now),
- in production (airing soon),
- planned (in development),
- upcoming (in development),
- pilot,
- canceled,
- or ended
 */

/*
* JSON Details ####################################################

// https://api.trakt.tv/shows/id
// id = {game-of-thrones}
 */

/*
{
  "title": "Game of Thrones",
  "year": 2011,
  "ids": {
    "trakt": 353,
    "slug": "game-of-thrones",
    "tvdb": 121361,
    "imdb": "tt0944947",
    "tmdb": 1399
  },
  "tagline": "Winter Is Coming",
  "overview": "Game of Thrones is an American fantasy drama television series created for HBO by David Benioff and D. B. Weiss. It is an adaptation of A Song of Ice and Fire, George R. R. Martin's series of fantasy novels, the first of which is titled A Game of Thrones.\n\nThe series, set on the fictional continents of Westeros and Essos at the end of a decade-long summer, interweaves several plot lines. The first follows the members of several noble houses in a civil war for the Iron Throne of the Seven Kingdoms; the second covers the rising threat of the impending winter and the mythical creatures of the North; the third chronicles the attempts of the exiled last scion of the realm's deposed dynasty to reclaim the throne. Through its morally ambiguous characters, the series explores the issues of social hierarchy, religion, loyalty, corruption, sexuality, civil war, crime, and punishment.",
  "first_aired": "2011-04-18T01:00:00.000Z",
  "airs": {
    "day": "Sunday",
    "time": "21:00",
    "timezone": "America/New_York"
  },
  "runtime": 60,
  "certification": "TV-MA",
  "network": "HBO",
  "country": "us",
  "updated_at": "2014-08-22T08:32:06.000Z",
  "trailer": null,
  "homepage": "http://www.hbo.com/game-of-thrones/index.html",
  "status": "returning series",
  "rating": 9,
  "votes": 111,
  "comment_count": 92,
  "languages": [
    "en"
  ],
  "available_translations": [
    "en",
    "tr",
    "sk",
    "de",
    "ru",
    "fr",
    "hu",
    "zh",
    "el",
    "pt",
    "es",
    "bg",
    "ro",
    "it",
    "ko",
    "he",
    "nl",
    "pl"
  ],
  "genres": [
    "drama",
    "fantasy"
  ],
  "aired_episodes": 50
}
 */
