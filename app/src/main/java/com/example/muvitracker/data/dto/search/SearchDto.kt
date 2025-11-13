package com.example.muvitracker.data.dto.search


import android.annotation.SuppressLint
import com.example.muvitracker.data.dto.movie.MovieBaseDto
import com.example.muvitracker.data.dto.show.ShowBaseDto
import com.example.muvitracker.data.dto.movie.toDomain
import com.example.muvitracker.data.dto.person.PersonBaseDto
import com.example.muvitracker.data.dto.person.toDomain
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.domain.model.SearchResult
import kotlinx.serialization.Serializable

// TODO: 1.1.3 - valori default per sottoclassi ok

// can be: movie || show || person
@SuppressLint("UnsafeOptInUsageError")
@Serializable

data class SearchDto(
    val type: String = "",
    val score: Double = 0.0,

    val movie: MovieBaseDto? = null,
    val show: ShowBaseDto? = null,
    val person: PersonBaseDto? = null
)


fun SearchDto.toDomain(): SearchResult {
    return when (type) {
        "movie" ->
            SearchResult.MovieItem(
                movieBase = movie!!.toDomain(),
                score = score
            )

        "show" ->
            SearchResult.ShowItem(
                showBase = show!!.toDomain(),
                score = score
            )

        "person" ->
            SearchResult.PersonItem(
                personBase = person!!.toDomain(),
                score = score
            )

        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
