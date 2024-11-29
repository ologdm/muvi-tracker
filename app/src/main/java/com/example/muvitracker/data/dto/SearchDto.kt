package com.example.muvitracker.data.dto


import com.example.muvitracker.data.dto.movie.MovieBaseDto
import com.example.muvitracker.data.dto.show.ShowBaseDto
import com.example.muvitracker.data.dto.movie.toDomain
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.data.dto.person.PersonBaseDto
import com.example.muvitracker.data.dto.person.toDomain
import com.example.muvitracker.domain.model.SearchResult

// can be: movie || show || person

data class SearchDto(
    val type: String,
    val score: Double,

    val movie: MovieBaseDto?,
    val show: ShowBaseDto?,
    val person: PersonBaseDto?
)


fun SearchDto.toDomain(): SearchResult {
    return when (type) {
        "movie" ->
            SearchResult.MovieItem(
                movie = movie!!.toDomain(),
                score = score
            )

        "show" ->
            SearchResult.ShowItem(
                show = show!!.toDomain(),
                score = score
            )

        "person" ->
            SearchResult.PersonItem(
                person = person!!.toDomain(),
                score = score
            )

        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
