package com.example.muvitracker.data.dto.movie

import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.base.Movie

// used for popular
data class MovieBaseDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun MovieBaseDto.toDomain(): Movie {
    return Movie(title, year, ids)
}















