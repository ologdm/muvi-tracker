package com.example.data.dto.movie

import android.annotation.SuppressLint
import com.example.domain.model.Ids
import com.example.domain.model.base.MovieBase
import kotlinx.serialization.Serializable

// used for popular
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MovieBaseDto(
    val title: String = "",
    val year: Int = -1,
    val ids: Ids // has default
)


fun MovieBaseDto.toDomain(): MovieBase {
    return MovieBase(
        title = title,
        year = year,
//        ids = ids.toDomain()
        ids = ids
    )
}















