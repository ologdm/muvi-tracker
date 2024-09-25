package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.base.Ids


data class SeasonExtended(
    val showId: Int,
    val seasonNumber: Int,
    val ids: Ids,
    val rating: Double,
    val episodeCount: Int,
    val airedEpisodes: Int,
    val title: String,
    val overview: String,
    val releaseYear: String,
    val network: String,
    // computed data from episodeRepository
    val watchedAll: Boolean = false, // default false
    val watchedCount: Int = 0, // default 0
    )
