package com.example.muvitracker.domain.model


import com.example.muvitracker.data.dto._support.Ids

data class Show(
    // DA DETAIL ENTITY

    // da trakt
    val year: Int?,
    val ids: Ids,
    val airedEpisodes: Int,

    // da tmdb
    val title: String?,
    val tagline: String?,
    val overview: String?,
    val status: String?,
    val firstAirDate: String?, // TODO new
    val lastAirDate: String?,// TODO new
    val runtime: Int?,
    val countries: List<String>, // TODO new
    val originalLanguage: String?,
//    languages NO
    val englishTitle: String?, // trakt
    val networks: List<String>, // ex -> network: String,
    val genres: List<String>,
    val youtubeTrailer: String?, // ex -> trailer: String,
    val homepage: String?,

    val traktRating: String?, // ex -> rating: String,
    val tmdbRating: String?,

    val backdropPath: String?, // TODO new
    val posterPath: String?, // TODO new

    val currentTranslation: String,


    // DA PREFS ENTITY
    val liked: Boolean, // default false
    val notes: String, // default ""
    val addedDateTime: Long?, // using timestamp

    // CALCOLATO DA EPISODE_TABLE
    val watchedCount: Int = 0, // computed data from episodeRepository

    // TODO: calcola dalla query di seasons il totale stagioni
    val seasonsCount: Int = 0,

) {

    // TICKET: watchedAll marked for shows not yet released (anticipated)
    val watchedAll: Boolean // stato  -> solo a livello di Ui
        get() = airedEpisodes > 0 && watchedCount >= airedEpisodes

}
