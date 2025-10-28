package com.example.muvitracker.data.dto._support

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ids(
    val trakt: Int = 0,
    val slug: String = "",
    val tvdb: Int = 0,
    val imdb: String = "", // for images
    val tmdb: Int = 0
) : Parcelable