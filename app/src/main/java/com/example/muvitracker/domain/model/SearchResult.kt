package com.example.muvitracker.domain.model

import com.example.muvitracker.domain.model.base.MovieBase
import com.example.muvitracker.domain.model.base.PersonBase
import com.example.muvitracker.domain.model.base.ShowBase


// TODO 1.1.3: sistemare i default per serialization OK
sealed interface SearchResult {

    data class MovieItem(
        val movieBase: MovieBase, // val default ok
        val score: Double = 0.0
    ) : SearchResult

    data class ShowItem(
        val showBase: ShowBase, // val default ok
        val score: Double = 0.0
    ) : SearchResult


    data class PersonItem(
        val personBase: PersonBase, // val default ok
        val score : Double = 0.0
    ) : SearchResult

}