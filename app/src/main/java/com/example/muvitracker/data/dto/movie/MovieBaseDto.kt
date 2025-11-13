package com.example.muvitracker.data.dto.movie

import android.annotation.SuppressLint
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.base.MovieBase
import kotlinx.serialization.Serializable

// used for popular
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MovieBaseDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun MovieBaseDto.toDomain(): MovieBase {
    return MovieBase(title, year, ids)
}















