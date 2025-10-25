package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.utilsdto.Ids


// TODO: 1.1.3 OK
data class DetailMovie(
    // from detail entity
    val year: Int?,
    val ids: Ids,
    //
    val title: String?,
    val tagline: String?,
    val overview: String?,
    val status: String?,
    val releaseDate: String?, // YYYY-MM-DD
    val runtime: Int?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val countries: List<String>, //  fare joinToString(", ")
    //
    val youtubeTrailer: String?,
    val homepage: String?,
    val traktRating: String?, // ?: "-"
    val tmdbRating: String?,  // ?: "-"
    val genres: List<String>,
    // TODO path link immagini - servono??
    val backdropPath: String?,  // /en971MEXui9diirXlogOrPKmsEn.jpg
    val posterPath: String?,    // /zoSiiUUzg2ny6uzuil7PbP13z53.jpg

    // from prefs entity
    val liked: Boolean,
    val watched: Boolean,
    val addedDateTime: Long?
) {

    // quick image for test
    fun imageUrl(): String {
        return "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
    }

}

