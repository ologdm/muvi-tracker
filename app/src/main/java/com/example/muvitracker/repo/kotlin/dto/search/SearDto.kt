package com.example.muvitracker.repo.kotlin.dto.search


import com.example.muvitracker.repo.kotlin.dto.search.principali.Episode
import com.example.muvitracker.repo.kotlin.dto.search.principali.Movie
import com.example.muvitracker.repo.kotlin.dto.search.principali.Show

// OK
data class SearDto (

    val type: String, // fisso
    val score: Double, //fisso

    val movie: Movie?, // classe K
    val show: Show?, // classe K
    val episode: Episode


)