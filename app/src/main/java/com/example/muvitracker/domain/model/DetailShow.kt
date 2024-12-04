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
    val rating: String, // converted from Float
    val votes: Int,
    val language: String,
    val languages: List<String>,
    val genres: List<String>,
    val airedEpisodes: Int,
    // prefs entity
    val liked: Boolean,
    val addedDateTime: Long?, // using timestamp
    // computed data from episodeRepository
    val watchedCount: Int = 0, // default 0
) {

    // TODO: edge case
    // stato  -> solo a livello di Ui
    val watchedAll: Boolean
        get() = watchedCount == airedEpisodes

}
