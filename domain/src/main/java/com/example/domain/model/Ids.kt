package com.example.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import kotlinx.serialization.Serializable


// FIXME: Ids Refactor -temporaneo, Separare con Ids Data
// parcelable - for ui comunitation / serializable - for data
@Serializable
@Parcelize
data class Ids(
    val trakt: Int = -1,
    val slug: String = "",
    val tvdb: Int = -1,
    val imdb: String = "",
    val tmdb: Int = -1
) : Parcelable

