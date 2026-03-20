package com.example.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


// FIXME: Ids Refactor -temporaneo, Separare con Ids Data
// Domain parcelable / data serializable
@Serializable
@Parcelize
data class Ids(
    val trakt: Int = -1,
    val slug: String = "",
    val tvdb: Int = -1,
    val imdb: String = "", // for images
    val tmdb: Int = -1
) : Parcelable

