package com.example.muvitracker.data.dto.basedto

import com.example.muvitracker.domain.model.base.Movie


data class MovieDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun MovieDto.toDomain(): Movie {
    return Movie(title, year, ids)
}










