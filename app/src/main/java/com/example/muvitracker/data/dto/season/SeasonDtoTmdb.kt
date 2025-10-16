package com.example.muvitracker.data.dto.season

import com.google.gson.annotations.SerializedName

// OK per db-v4
data class SeasonDtoTmdb(
//    val _id: String, // id interno tmdb, non serve
//    @SerializedName("air_date") val airDate: String,
//    val episodes: List<EpisodeDto>,
//    val crew: List<CrewMemberDto>
    val name: String, // Stagione 1 (tradotta)
//    val networks : List<NetworkDto>
    val overview: String,
    val id: Int, // id tmdb
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("vote_average") val voteAverage: Double
)