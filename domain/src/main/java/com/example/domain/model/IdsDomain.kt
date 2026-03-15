package com.example.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


//@Serializable // non serve
@Parcelize
data class IdsDomain(
    val trakt: Int = -1,
    val slug: String = "",
    val tvdb: Int = -1,
    val imdb: String = "", // for images
    val tmdb: Int = -1
) : Parcelable