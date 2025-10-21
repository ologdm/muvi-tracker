package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.utilsdto.Ids


data class SeasonExtended(
    // construtor - 11 attributi
    val showId: Int,
    val seasonNumber: Int,
    val ids: Ids,
    val traktRating: String?,
    val episodeCount: Int, // DEFAULT = 0 a db
    val airedEpisodes: Int, // DEFAULT = 0 a db
    val title: String?,
    val overview: String?,
//    val releaseYear: String,
    val releaseDate: String?, // 1.1.3 nullable per compatibilità dao
    val network: String?,
    // computed data from episodeRepository
    val watchedCount: Int = 0, // calcolato a DB (default0) - calcola elementi guardati su totale
) {
    // property al posto di fun
    val watchedAll: Boolean
        get() = (episodeCount > 0) && (watchedCount == episodeCount)
}

// SeasonEntity -> SeasonExtended : on SeasonDao


