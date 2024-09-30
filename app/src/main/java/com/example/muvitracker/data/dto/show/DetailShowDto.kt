package com.example.muvitracker.data.dto.show

import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.data.dto.base.Ids
import com.google.gson.annotations.SerializedName

// complete dto, commented if not used
// all dto element nullable -> all default values on toDomain()

data class DetailShowDto(
    val title: String?,
    val year: Int?, // start
    val ids: Ids, // + tvdb

    val tagline: String?, // the winter is coming
    val overview: String?, // about movie
    @SerializedName("first_aired") val firstAired: String?,
//    val airs : Airs // day, time, timezone
    val runtime: Int? = 0, // 60
//    val certification: String,
    val network: String?, // hbo
    val country: String?, // us
//    @SerializedName("updated_at") val updatedAt: String = "", // date
    val trailer: String?, // link sito
    val homepage: String?, // website
    val status: String?, // coming soon, wip, ended
    val rating: Float?,
    val votes: Int?,
//    @SerializedName("comment_count") val commentCount: Int = 0,
    val language: String?,
    val languages: List<String>?,
//    val availableTranslations: List<String> = emptyList(),
    val genres: List<String>?,
    @SerializedName("aired_episodes") val airedEpisodes: Int?
)


fun DetailShowDto.toEntity(): DetailShowEntity {
    return DetailShowEntity(
        traktId = ids.trakt,
        title = title ?: "N/A",
        year = year ?: 0,
        ids = ids,
        //
        tagline = tagline ?: "N/A",
        overview = overview ?: "N/A",
        firstAired = firstAired ?: "N/A",
        runtime = runtime ?: 0,
        network = network ?: "N/A",
        country = country ?: "N/A",
        trailer = trailer ?: "N/A",
        homepage = homepage ?: "N/A",
        status = status ?: "N/A",
        rating = rating ?: 0f,
        votes = votes ?: 0,
        language = language ?: "N/A",
        languages = languages ?: emptyList(),
        genres = genres ?: emptyList(),
        airedEpisodes = airedEpisodes ?: 0
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
