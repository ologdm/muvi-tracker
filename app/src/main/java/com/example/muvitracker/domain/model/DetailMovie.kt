package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.base.Ids

data class DetailMovie(
    // detail entity
    val title: String,
    val year: Int,
    val ids: Ids,
    val overview: String,
    val released: String,
    val runtime: Int,
    val country: String,
    val rating: Float,
    val genres: List<String>,
    // prefs entity
    val liked: Boolean,
    val watched: Boolean,
    val addedDateTime: Long?
) {

    fun imageUrl(): String {
        return "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
    }

}

