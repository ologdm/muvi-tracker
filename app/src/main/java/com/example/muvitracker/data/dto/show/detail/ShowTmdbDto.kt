package com.example.muvitracker.data.dto.show.detail

import com.example.muvitracker.data.dto.movie.detail.GenreDto
import com.example.muvitracker.data.dto.movie.detail.VideosResult
import com.example.muvitracker.data.dto.season.SeasonTmdbDto
import com.google.gson.annotations.SerializedName


// tmdb dto - game of thrones - 1399 - it
data class ShowTmdbDto(
//    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?, // "/zZqpAXxVSBtxV9qPBcscfXBcL2w.jpg"
//    @SerializedName("created_by") val createdBy: List<CreatorDto>, // NO
    @SerializedName("episode_run_time") val episodeRunTime: List<Int>?, // [] - rimane vuoto su tmdb
    @SerializedName("first_air_date") val firstAirDate: String?, // "2011-04-17"
    val genres: List<GenreDto>?, // Sci-Fi & Fantasy, Dramma
    val homepage: String?, // http://www.hbo.com/game-of-thrones
    val id: Int,
//    @SerializedName("in_production") val inProduction: Boolean,
    val languages: List<String>?, // ["en"]
    @SerializedName("last_air_date") val lastAirDate: String?, // 2019-05-19
//    @SerializedName("last_episode_to_air") val lastEpisodeToAir: EpisodeDto?, // NO
    val name: String?, // Il Trono di Spade
//    @SerializedName("next_episode_to_air") val nextEpisodeToAir: EpisodeDto?, // NO
    val networks: List<NetworkDto>?, //  [HBO] - chi l'ha prodotto -> HBO
//    @SerializedName("number_of_episodes") val numberOfEpisodes: Int, // 73  NO
//    @SerializedName("number_of_seasons") val numberOfSeasons: Int, //  8  NO
    @SerializedName("origin_country") val originCountry: List<String>?, // ["US"]
    @SerializedName("original_language") val originalLanguage: String?, // en
    @SerializedName("original_name") val originalName: String?, // Games of Thrones
    val overview: String?, // // "Nell'immaginario mondo di Westeros..."
//    val popularity: Double?, // 111.3636
    @SerializedName("poster_path") val posterPath: String?, // "/n4IxkaZanXQU89WNRxt8h1fw1yb.jpg"
//    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyDto>,
//    @SerializedName("production_countries") val productionCountries: List<ProductionCountryDto>,
    val seasons: List<SeasonTmdbDto>?, // !!! SOLO STAGIONI REPO
//    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguageDto>?, // NO, come languages ma piu complet
    val status: String?, // "Returning Series"
    val tagline: String?, // "L'inverno sta arrivando."
//    val type: String, // NO - Scripted;  altre: Reality, Documentary, Talk Show
    @SerializedName("vote_average") val voteAverage: Double?, // 8.458,
//    @SerializedName("vote_count") val voteCount: Int, // 25630 - NO
    val videos: VideosResult? // uguale a movies
)


data class NetworkDto (
    val id: Int, // es. 49
    val name: String, // es. "HBO"
    @SerializedName("logo_path") val logoPath: String?, // es. "/tuomYjQdUqk2H1Zs2kA6dRpzQ7y.png"
    @SerializedName("origin_country") val originCountry: String? // es. "US"
)


//data class CreatorDto(
//    val id: Int,
//    @SerializedName("credit_id") val creditId: String,
//    val name: String,
//    @SerializedName("original_name") val originalName: String,
//    val gender: Int,
//    @SerializedName("profile_path")
//    val profilePath: String?
//)
//
//data class EpisodeDto(
//    val id: Int,
//    val name: String,
//    val overview: String,
//    @SerializedName("vote_average") val voteAverage: Double,
//    @SerializedName("vote_count") val voteCount: Int,
//    @SerializedName("air_date") val airDate: String,
//    @SerializedName("episode_number") val episodeNumber: Int,
//    @SerializedName("episode_type") val episodeType: String,
//    @SerializedName("production_code") val productionCode: String,
//    val runtime: Int,
//    @SerializedName("season_number") val seasonNumber: Int,
//    @SerializedName("show_id") val showId: Int,
//    @SerializedName("still_path") val stillPath: String?
//)
//
//data class NetworkDto(
//    val id: Int,
//    @SerializedName("logo_path") val logoPath: String?,
//    val name: String,
//    @SerializedName("origin_country") val originCountry: String
//)