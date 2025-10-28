package com.example.muvitracker.data.dto.movie

import com.example.muvitracker.domain.model.base.Movie
import com.google.gson.annotations.SerializedName

data class AnticipatedDtoM(
    @SerializedName("list_count") val listCount: Int,
    val movie: MovieBaseDto
)


fun AnticipatedDtoM.toDomain(): Movie {
    return Movie(
        title = movie.title,
        year = movie.year,
        ids = movie.ids
    )
}
