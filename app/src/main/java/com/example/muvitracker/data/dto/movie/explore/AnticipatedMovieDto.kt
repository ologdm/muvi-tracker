package com.example.muvitracker.data.dto.movie.explore

import com.example.muvitracker.data.dto.movie.MovieBaseDto
import com.example.muvitracker.domain.model.base.MovieBase
import com.google.gson.annotations.SerializedName

data class AnticipatedDtoM(
    @SerializedName("list_count") val listCount: Int,
    val movie: MovieBaseDto
)


fun AnticipatedDtoM.toDomain(): MovieBase {
    return MovieBase(
        title = movie.title,
        year = movie.year,
        ids = movie.ids
    )
}
