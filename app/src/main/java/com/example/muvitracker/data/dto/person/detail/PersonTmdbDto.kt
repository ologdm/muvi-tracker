package com.example.muvitracker.data.dto.person.detail

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PersonTmdbDto(
    @SerialName("also_known_as") val alsoKnownAs: List<String>?,
    val biography: String?,
    val birthday: String?, // "1944-05-14" yyyy-MM-dd
    val deathday: String?,
    val gender: Int?, // 2
    val homepage: String?,  // Può essere null
    val id: Int,
    @SerialName("known_for_department") val knownForDepartment: String?, // "Directing",
    val name: String?, // "George Lucas"
    @SerialName("place_of_birth") val placeOfBirth: String?, // "Modesto, California, USA"
    val popularity: Double?, // 1.5481,
    @SerialName("profile_path") val profilePath: String? // "Modesto, California, USA"

    //    val adult: Boolean,
    //    @SerialName("imdb_id") val imdbId: String,
)