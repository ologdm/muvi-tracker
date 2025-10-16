package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.utilsdto.Ids


data class SeasonExtended(
    val showId: Int,
    val seasonNumber: Int,
    val ids: Ids,
    val rating: String,
    val episodeCount: Int,
    val airedEpisodes: Int,
    val title: String,
    val overview: String,
    val releaseYear: String,
    val network: String,
    // computed data from episodeRepository
    val watchedCount: Int = 0, // default 0
) {
    // property al posto di fun
    val watchedAll: Boolean
        get() = (episodeCount > 0) && (watchedCount == episodeCount)
}

// SeasonEntity -> SeasonExtended : on SeasonDao


