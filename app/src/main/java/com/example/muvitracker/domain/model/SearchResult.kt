package com.example.muvitracker.domain.model

import com.example.muvitracker.domain.model.base.Episode
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.model.base.Show



sealed interface SearchResult {

    data class MovieItem(
        val movie: Movie,
        val score: Double
    ) : SearchResult

    data class ShowItem(
        val show: Show,
        val score: Double
    ) : SearchResult


}