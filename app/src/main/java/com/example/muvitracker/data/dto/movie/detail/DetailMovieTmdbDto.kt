package com.example.muvitracker.data.dto.movie.detail

import com.google.gson.annotations.SerializedName

// tutti elementi nullable

// tmdb dto - deadpool - 293660 - it
data class DetailMovieDtoTmdb(
//    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
//    @SerializedName("belongs_to_collection")
//    val belongsToCollection: BelongsToCollection?,
//    val budget: Int,
    val genres: List<GenreDto>?,
//    val homepage: String?,
    @SerializedName("homepage") val homepage: String?, // http://www.20thcenturystudios.com/movies/deadpool
//    val id: Int,
    @SerializedName("id") val id: Int, // tmdb id
//    val imdbId: String?,
//    @SerializedName("origin_country")
    @SerializedName("origin_country")val originCountry: List<String>?, // US,GB - iso_3166_1
    @SerializedName("original_language") val originalLanguage: String?, // "en" - iso_639_1
    @SerializedName("original_title") val originalTitle: String?,
    val overview: String?, // The origin story of former Special Forces ...
//    val popularity: Double, // 13.3693
    @SerializedName("poster_path") val posterPath: String?,
//    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>,
//    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date") val releaseDate: String?, // 2016-02-09
//    val revenue: Long,
    val runtime: Int?, // 108
//    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    val status: String?, // "Released"
    val tagline: String?, // X
    val title: String?, // X
//    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double?, // 7.6 tmdb
//    @SerializedName("vote_count")
//    val voteCount: Int
    val videos: VideosResult? // lista video - trailer
)



// CLASSI AGGIUNTIVE ---------------------------------------------------------------
data class VideosResult(
    val results: List<VideoDto>
)

data class VideoDto(
    val key: String, // result = "CzCEe0svL7k" -> "https://www.youtube.com/watch?v= + $result"
    val site: String, // 95% youtube
    val size: String, // 1080
    val type: String, // Trailer, Featurette, Teaser
    val official: Boolean
)

data class GenreDto(
    val id: Int,
    val name: String  // action
)


/**
 * priorità:
 *    1. trailer ufficiali,
 *    2. trailer con risoluzione > 720p
 *    3. se return null -> prenderà trailer in inglese da trakt
 */
fun VideosResult.youtubeLinkTransformation(): String? {
    // TODO eugi sistemare
//    results.filter { it.official }
//        .sortedByDescending { it.size }
//        .sortedByDescending(compareValues({ it.nonme }, { it.cogme }))
//        .firstOrNull()

    val video = results.firstOrNull { it.site == "YouTube" && it.type == "Trailer" && it.official } // 1.priorita official, anche
            ?: results.firstOrNull { it.site == "YouTube" && it.type == "Trailer" && it.size.toInt()>720 } // 2. priorità trailer
//            ?: results.firstOrNull { it.site == "YouTube" } // 3. fallback a qualsiasi YouTube
    // else prende quello di trakt in inglese

    return video?.key?.let { "https://www.youtube.com/watch?v=${it}" }
}


// struttura JSON video ---------------------------------------------------------
/*
"results": [
    {
        "iso_639_1": "it",
        "iso_3166_1": "IT",
        "name": "Deadpool | Trailer Ufficiale Redband #2 [HD] | 20th Century Fox",
        "key": "yAX6uis3Eqc",
        "published_at": "2015-12-25T08:01:13.000Z",
        "site": "YouTube",
        "size": 1080,
        "type": "Trailer",
        "official": true,
        "id": "5692c4f692514144530006c2"
    },
    {
        ...
        ...
    }
 */



