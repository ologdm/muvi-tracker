package com.example.muvitracker.data.dto.show.detail

import android.annotation.SuppressLint
import com.example.muvitracker.data.LanguageManager
import com.example.muvitracker.data.database.entities.ShowEntity
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.utils.orIfBlank
import com.example.muvitracker.data.utils.orIfEmpty
import com.example.muvitracker.data.utils.splitToCleanList
import com.example.muvitracker.data.utils.youtubeLinkTransformation
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.formatDateFromFirsAired
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// only base + rating

/**
 * serialization:
 * valori default vengono considerati
 * mettere null nei campi incerti permette in futuro di non falire se il server decide di non inviarci piu quei dati
 */


// trakt dto - game of thrones - 1390 - en
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ShowTraktDto(
    val title: String?,
    val year: Int? , // 2011 - first year
    val ids: Ids, // + tvdb

    val tagline: String? = null, // Winter is coming
    val overview: String? = null, // Seven noble families fight.....
    @SerialName("first_aired") val firstAired: String? = null, // "2011-04-18T01:00:00.000Z"
//    val airs : Airs // [ day, time, timezone ]
    val runtime: Int? = null, // 55
//    val certification: String,
    val network: String? = null, // HBO
    val country: String? = null, // us
//    @SerializedName("updated_at") val updatedAt: String = "", // date
    val trailer: String? = null, // https://youtube.com/watch?v=KPLWWIOCOOQ
    val homepage: String? = null, // http://www.hbo.com/game-of-thrones
    val status: String? = null, // ended
    val rating: Float? = null, // 8.89378
    val votes: Int? = null, // 142022
//    @SerializedName("comment_count") val commentCount: Int = 0, // 444
    val language: String? = null, // en
    val languages: List<String>? = null, // [en, it]
//    val availableTranslations: List<String> = emptyList(), // [en, it, tr, ...]
    val genres: List<String>? = null, // [drama, fantasy]
    @SerialName("aired_episodes") val airedEpisodes: Int? = null, // 733
    @SerialName("original_title")
    val originalTitle: String? = null // Games of thrones
)


/**  solo da trakt:  ids, year, , rating, airedEpisodes  */
/**  ## Logica di unione, uguale a movie */

fun mergeShowsDtoToEntity(
    trakt: ShowTraktDto, tmdb: ShowTmdbDto?
): ShowEntity {
    return ShowEntity(
        // trakt ok
        traktId = trakt.ids.trakt,
        year = trakt.year,
        ids = trakt.ids,
        airedEpisodes = trakt.airedEpisodes
            ?: 0, // default = 0 (logica vecchia serve per calcolo ())

        // tmdb
        title = tmdb?.name.orIfBlank(trakt.title),
        tagline = tmdb?.tagline.orIfBlank(trakt.tagline),
        overview = tmdb?.overview.orIfBlank(trakt.overview),
        status = tmdb?.status.orIfBlank(trakt.status),
        firstAirDate = tmdb?.firstAirDate.orIfBlank(trakt.firstAired.formatDateFromFirsAired()), // test con tmdb
        lastAirDate = tmdb?.lastAirDate, // lasciare null, per logica ui
        runtime = trakt.runtime, // only trakt
        countries = tmdb?.originCountry
            .orIfEmpty(trakt.country?.splitToCleanList()?.map { it.uppercase() })
            ?: emptyList(),
        originalLanguage = tmdb?.originalLanguage.orIfBlank(trakt.language),
        languages = tmdb?.languages.orIfEmpty(trakt.languages)
            ?: emptyList(), // entity not null
        originalTitle = tmdb?.originalName.orIfBlank(trakt.originalTitle),
        englishTitle = trakt.title, // di default trakt è tutto in inglese
        networks = tmdb?.networks?.map { it.name }
            .orIfEmpty(trakt.network?.splitToCleanList())
            ?: emptyList(),
        genres = tmdb?.genres?.map { it.name }
            .orIfEmpty(trakt.genres)
            ?: emptyList(), // entity not null
        //
        youtubeTrailer = tmdb?.videos?.youtubeLinkTransformation().orIfBlank(trakt.trailer),
        homepage = tmdb?.homepage.orIfBlank(trakt.homepage),
        //
        backdropPath = tmdb?.backdropPath, // solo tmdb
        posterPath = tmdb?.posterPath, // solo tmdb
        // ratings
        traktRating = trakt.rating?.firstDecimalApproxToString(),
        tmdbRating = tmdb?.voteAverage?.firstDecimalApproxToString(), // in 8.458 -> out 8.5

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
