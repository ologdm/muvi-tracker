package com.example.muvitracker.data.dto._support

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError") // (kotlin.serialization + parcelize insieme) -> non è un errore critico, tutto funziona corettamente
@Serializable
@Parcelize
data class Ids(
    val trakt: Int = -1,
    val slug: String = "",
    val tvdb: Int = -1,
    val imdb: String = "", // for images
    val tmdb: Int = -1
) : Parcelable