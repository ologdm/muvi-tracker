package com.example.muvitracker.data.repositories.imagetmdb.dto

import com.google.gson.annotations.SerializedName

// attivi solo i campi utilizzati per ora

data class TmdbShowDto(
//    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
//    @SerializedName("created_by") val createdBy: List<CreatorDto>,
//    @SerializedName("episode_run_time") val episodeRunTime: List<Int>,
//    @SerializedName("first_air_date") val firstAirDate: String?,
//    val genres: List<GenreDto>,
    val homepage: String?,
    val id: Int,
//    @SerializedName("in_production") val inProduction: Boolean,
//    val languages: List<String>,
//    @SerializedName("last_air_date") val lastAirDate: String?,
//    @SerializedName("last_episode_to_air") val lastEpisodeToAir: EpisodeDto?,
//    val name: String,
//    @SerializedName("next_episode_to_air") val nextEpisodeToAir: EpisodeDto?,
//    val networks: List<NetworkDto>,
//    @SerializedName("number_of_episodes") val numberOfEpisodes: Int,
//    @SerializedName("number_of_seasons") val numberOfSeasons: Int,
//    @SerializedName("origin_country") val originCountry: List<String>,
//    @SerializedName("original_language") val originalLanguage: String,
//    @SerializedName("original_name") val originalName: String,
//    val overview: String,
//    val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
//    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyDto>,
//    @SerializedName("production_countries") val productionCountries: List<ProductionCountryDto>,
//    val seasons: List<SeasonDto>
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
//data class GenreDto(
//    val id: Int,
//    val name: String
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