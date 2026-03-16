package com.example.data.dto.movie.explore

import android.annotation.SuppressLint
import com.example.data.dto._support.toDomain
import com.example.data.dto.movie.MovieBaseDto
import com.example.domain.model.base.MovieBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AnticipatedDtoM(
    @SerialName("list_count") val listCount: Int,
    val movie: MovieBaseDto
)


fun AnticipatedDtoM.toDomain(): MovieBase {
    return MovieBase(
        title = movie.title,
        year = movie.year,
        ids = movie.ids.toDomain()
    )
}
