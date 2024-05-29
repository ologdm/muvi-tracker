package com.example.muvitracker.inkotlin.data.dto.search


import com.example.muvitracker.inkotlin.data.dto.search.Internal.Episode
import com.example.muvitracker.inkotlin.data.dto.search.Internal.Movie
import com.example.muvitracker.inkotlin.data.dto.search.Internal.Show


data class SearDto (

    val type: String, // fisso
    val score: Double, //fisso

    val movie: Movie?, // classe K
    val show: Show?, // classe K
    val episode: Episode


)