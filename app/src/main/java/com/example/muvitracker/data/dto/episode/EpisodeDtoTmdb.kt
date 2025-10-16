package com.example.muvitracker.data.dto.episode

import com.google.gson.annotations.SerializedName

data class EpisodeDtoTmdb(
//    @SerializedName("air_date") val airDate: String,
//    val crew: List<CrewMemberDto>,
//    @SerializedName("episode_number")val episodeNumber :Int,
//    @SerializedName("guest_stars") val guestStars: List<GuestStarDto>
//    val name :String,
//    val overview: String,
    val id: Int,
//    @SerializedName("production_code") val productionCode: String?,
//    val runtime: Int,
//    @SerializedName("season_number")val seasonNumber :Int,
    @SerializedName("still_path") val stillPath: String?,
//    @SerializedName("vote_average") val voteAverage: Double,
//    @SerializedName("vote_count") val voteCount: Int,
)