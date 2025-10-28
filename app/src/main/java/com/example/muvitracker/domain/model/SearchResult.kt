package com.example.muvitracker.domain.model

import com.example.muvitracker.domain.model.base.MovieBase
import com.example.muvitracker.domain.model.base.PersonBase
import com.example.muvitracker.domain.model.base.ShowBase



sealed interface SearchResult {

    data class MovieItem(
        val movieBase: MovieBase,
        val score: Double
    ) : SearchResult

    data class ShowItem(
        val showBase: ShowBase,
        val score: Double
    ) : SearchResult


    data class PersonItem(
        val personBase: PersonBase,
        val score : Double
    ) : SearchResult

}