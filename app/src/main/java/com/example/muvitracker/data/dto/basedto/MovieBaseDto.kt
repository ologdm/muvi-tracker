package com.example.muvitracker.data.dto.basedto

import com.example.muvitracker.data.database.entities.PopularMovieEntity
import com.example.muvitracker.domain.model.base.Movie


data class MovieBaseDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun MovieBaseDto.toDomain(): Movie {
    return Movie(title, year, ids)
}


// TODO
fun MovieBaseDto.toEntity(): PopularMovieEntity {
    return PopularMovieEntity(
        traktId = ids.trakt,
        title = title,
        year = year,
        ids = ids
    )
}













