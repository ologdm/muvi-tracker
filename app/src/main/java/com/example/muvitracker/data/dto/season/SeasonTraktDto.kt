package com.example.muvitracker.data.dto.season

import android.os.Parcelable
import com.example.muvitracker.data.LanguageManager
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/** dto principale,
 * per season il tmdbDto e solo di supporto
 */
@Parcelize
data class SeasonTraktDto(
    val number: Int?,
    val ids: Ids,
    val rating: Double?,
//    val votes: Int?, // not use
    @SerializedName("episode_count")
    val episodeCount: Int?, // tot planned
    @SerializedName("aired_episodes")
    val airedEpisodes: Int?, // tot released
    val title: String?, // "season 1" // tmdb
    val overview: String?, // tmdb
    @SerializedName("first_aired") val firstAired: String?, // tmdb
//    @SerializedName("updated_at") val updatedAt: String?, // not use
    val network: String? // tmdb
) : Parcelable {

//    fun getYear(): String? {
//        return firstAired?.substring(0, 4)
//    }
}


/**
 * Logica voluta:
 *  integra TmdbDTo a TraktDTo, per traduzioni e immagini,
 *  ma se manca Tmdb usa solo trakt
 */
fun mergeSeasonsDtoToEntity(
    showId: Int,
    trakt: SeasonTraktDto,
    tmdb: SeasonTmdbDto?,
): SeasonEntity {
    return SeasonEntity(
        seasonTraktId = trakt.ids.trakt,
        seasonNumber = trakt.number
            ?: -1,
        ids = trakt.ids,
        showId = showId,
        episodeCount = trakt.episodeCount ?: 0, // default = 0, per calcolo a db
        airedEpisodes = trakt.airedEpisodes ?: 0, // default = 0, per calcolo a db
        network = trakt.network, // su tmdb non disponibile
        // tmdb
        title = tmdb?.name
            ?: trakt.title,
        overview = tmdb?.overview
            ?: trakt.overview,
        // JSON - tmdb "2010-12-05" ; // trakt "2016-07-15T07:00:00.000Z"
        airDate = (tmdb?.airDate ?: trakt.firstAired)?.take(10),  // prendo primi 10 elementi
        traktRating = trakt.rating?.firstDecimalApproxToString(), // OK
        tmdbRating = tmdb?.voteAverage?.firstDecimalApproxToString(), // OK
        posterPath = tmdb?.posterPath,

        currentTranslation = LanguageManager.getSystemLocaleTag()
    )
}



/*
     {
        "number": 0,
        "ids": {
            "trakt": 3962,
            "tvdb": 137481,
            "tmdb": 3627,
            "tvrage": null
        },
        "rating": 8.29189,
        "votes": 370,
        "episode_count": 286,
        "aired_episodes": 286,
        "title": "Specials",
        "overview": null,
        "first_aired": "2010-12-06T02:00:00.000Z",
        "updated_at": "2024-08-13T18:54:50.000Z",
        "network": "HBO"
    }
 */

