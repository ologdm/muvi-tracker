package com.example.domain.model


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
    val firstAirDate: String?, // new
    val lastAirDate: String?,// new
    val runtime: Int?,
    val countries: List<String>, // new
    val originalLanguage: String?,
//    languages NO
    val englishTitle: String?, // trakt
    val networks: List<String>, // ex -> network: String,
    val genres: List<String>,
    val youtubeTrailer: String?, // ex -> trailer: String,
    val homepage: String?,

    val traktRating: String?, // ex -> rating: String,
    val tmdbRating: String?,
    val imdbRating: String?,
    val rottenTomatoesRating: String?,

    val backdropPath: String?, // new
    val posterPath: String?, // new

    val currentTranslation: String,


    // DA PREFS ENTITY
    val liked: Boolean, // default false
    val notes: String, // default ""
    val addedDateTime: Long?, // using timestamp

    // CALCOLATO DA EPISODE_TABLE
    val watchedCount: Int = 0, // computed data from episodeRepository

    // NOTE: calculated from the seasons query for total seasons
    val seasonsCount: Int = 0,

    ) {

    // TICKET: watchedAll marked for shows not yet released (anticipated)
    val watchedAll: Boolean // stato  -> solo a livello di Ui
        get() = airedEpisodes > 0 && watchedCount >= airedEpisodes

}
