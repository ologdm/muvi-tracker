package com.example.muvitracker.data.repositories.imagetmdb.dto

import com.google.gson.annotations.SerializedName

// attivi solo i campi utilizzati per ora

data class TmdbSeasonDto(
//    val _id: String,
//    @SerializedName("air_date") val airDate: String,
//    val episodes: List<EpisodeDto>,
//    val crew: List<CrewMemberDto>
    val name: String,
    val overview: String,
    val id: Int,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("vote_average") val voteAverage: Double
)