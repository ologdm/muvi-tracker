package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.base.Ids

data class DetailMovie(
    // detail entity
    val title: String,
    val year: Int,
    val ids: Ids,
    val tagline: String, // new
    val overview: String,
    val released: String, // converted -> (from yyyy-MM-dd in MMMyyy)
    val runtime: Int,
    val country: String,
    val trailer: String, // new
    val homepage: String, // new
    val status: String, // new
    val rating: String, // converted from Float
    val votes: Int, // new
    val language: String, // new
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

