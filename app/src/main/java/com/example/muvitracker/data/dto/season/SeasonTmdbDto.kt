package com.example.muvitracker.data.dto.season

import com.google.gson.annotations.SerializedName

// TODO: OK
/**
 * DTO di supporto a TRAKT per traduzione e immagini
 * da chiamata da show, comprende anche i season
 */
data class SeasonTmdbDto(
    @SerializedName("air_date") val airDate: String?,
//    @SerializedName("episode_count") val episodeCount: Int,
    val id: Int, // id tmdb
    val name: String?, // Stagione 1 (tradotta)
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("season_number") val seasonNumber: Int?,
    @SerializedName("vote_average") val voteAverage: Double?
)


// DTO SEASON COMPLETA, CON EPISODI -NOT USED -------------------------
// chiamata da es:   /tv/{1399}/season/1

//data class SeasonTmdbDto(
////    val _id: String, // id interno tmdb, non serve
//    @SerializedName("air_date") val airDate: String,
////    val episodes: List<EpisodeDto>, // NO
////    val crew: List<CrewMemberDto> // NO
//    val name: String, // Stagione 1 (tradotta)
//    val overview: String?,
//    val id: Int, // id tmdb
//    @SerializedName("poster_path") val posterPath: String?,
//    @SerializedName("season_number") val seasonNumber: Int?,
//    @SerializedName("vote_average") val voteAverage: Double?
//)
