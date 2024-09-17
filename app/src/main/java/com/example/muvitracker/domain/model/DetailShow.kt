package com.example.muvitracker.domain.model


import com.example.muvitracker.data.dto.base.Ids

// 00
data class DetailShow(
    // detail entity
    val title: String,
    val year: Int,
    val ids: Ids,
    val tagline: String,
    val overview: String,
    val firstAired: String,
    val runtime: Int,
    val network: String,
    val country: String,
    val trailer: String,
    val homepage: String,
    val status: String,
    val rating: Float,
    val votes: Int,
    val language: String,
    val languages: List<String>,
    val genres: List<String>,
    val airedEpisodes: Int,

    // prefs entity
    val liked: Boolean,
    val watchedAll: Boolean, // solo
    val watchedCount: Int, // default 0,
    val addedDateTime: Long?, // using timestamp

    ) {
    fun imageUrl(): String {
        return "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
    }
    // imdb univoco tra show e movie
}
