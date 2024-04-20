package com.example.muvitracker.inkotlin.model.dto.support

data class Ids(
    val trakt: Int = 0,
    var slug: String = "",
    val imdb: String = "",  // utilizzata per immagini
    val tmdb: Int = 0
)
