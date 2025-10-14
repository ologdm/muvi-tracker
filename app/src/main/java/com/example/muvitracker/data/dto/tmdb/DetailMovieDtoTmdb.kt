package com.example.muvitracker.data.dto.tmdb

import com.example.muvitracker.data.LanguageManager
import com.example.muvitracker.data.database.entities.DetailMovieEntityTmdb
import com.google.gson.annotations.SerializedName

// TODO 1.1.3 traduzioni
//    overview, tagline, title, voteAverage tmdb
//    status (no)


data class DetailMovieDtoTmdb(
//    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
//    @SerializedName("belongs_to_collection") val belongsToCollection: BelongsToCollection?,
//    val budget: Int,
    val genres: List<GenreDto>?, // tradotto
//    @SerializedName("homepage") val homepage: String?,
    @SerializedName("id") val id: Int,
//    val imdbId: String?,
//    @SerializedName("origin_country") val originCountry: List<String>, // ["US"]
//    @SerializedName("original_language") val originalLanguage: String?, // "en"
//    @SerializedName("original_title") val originalTitle: String,
    val overview: String?,
//    val popularity: Double, // 13.3693
    @SerializedName("poster_path") val posterPath: String?,
//    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>,
//    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
//    @SerializedName("release_date") val releaseDate: String,
//    val revenue: Long,
//    val runtime: Int?,
//    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    val status: String, // "Released"
    val tagline: String?,
    val title: String,
//    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
//    @SerializedName("vote_count") val voteCount: Int
    val videos: VideosResult
)

val systemLanguage = LanguageManager.getSystemLocaleTag()

fun DetailMovieDtoTmdb.toEntity(): DetailMovieEntityTmdb {
    return DetailMovieEntityTmdb(
        tmdbId = id,
        translation = systemLanguage,
        title = title,
        tagline = tagline,
        overview = overview,
        status = status,
        voteTmdb = voteAverage,
        trailerLink = videos.youtubeLinkTransformation(), // nullable
        genres = genres?.map { it.name } ?: emptyList(),
        backdropPath = backdropPath,
        posterPath = posterPath
    )
}


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
    val name: String
)


// deve essere nullable, per verificare se ho risultato da tmdb
fun VideosResult.youtubeLinkTransformation(): String? {
    // trova officiale
    val video = results.firstOrNull { video ->
        video.official && video.site == "YouTube" && video.type == "Trailer"
    }
    // trova non officiale
    if (video == null) {
        results.firstOrNull { video ->
            video.site == "YouTube" && video.type == "Trailer"
        }
    }

    return video?.key?.let { "https://www.youtube.com/watch?v=${it}" }
}


// struttura JSON video
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
 */



