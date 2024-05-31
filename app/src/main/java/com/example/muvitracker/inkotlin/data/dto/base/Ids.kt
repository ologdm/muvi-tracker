package com.example.muvitracker.inkotlin.data.dto.base

data class Ids (
    val trakt : Int = 0,
    val slug: String = "",
    val imdb: String = "", // utilizzata per immagini
    val tvdb :Int = 0,
    val tmdb :Int = 0
)
