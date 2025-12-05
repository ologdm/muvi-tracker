package com.example.muvitracker.data.dto.episode

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// tmdb - solo name, overview, stillPath, 1.1.3 OK
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class EpisodeTmdbDto(
//    @SerializedName("air_date") val airDate: String?,
    @SerialName("episode_number")val episodeNumber :Int?, // serve per ins a store, check con trakt
//    @SerializedName("episode_type") val episodeType: String?,
    val name :String?,
    val overview: String?,
    val id: Int,
//    @SerializedName("production_code") val productionCode: String?,
//    val runtime: Int?,
    @SerialName("season_number")val seasonNumber :Int?, // serve per ins a store, check con trakt
    @SerialName("still_path") val stillPath: String?,
//    @SerializedName("vote_average") val voteAverage: Double,
//    @SerializedName("vote_count") val voteCount: Int,
//    val crew: List<CrewMemberDto>,
//    @SerializedName("guest_stars") val guestStars: List<GuestStarDto>
)