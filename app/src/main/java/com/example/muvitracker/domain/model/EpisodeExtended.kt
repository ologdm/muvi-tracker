package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.utilsdto.Ids

data class EpisodeExtended (
    val episodeTraktId: Int,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val title: String,
    val ids: Ids,
    val showId: Int,
    //
    val numberAbs: Int,
    val overview: String,
    val rating: String,
    val firstAiredFormatted: String?,
    val availableTranslations: List<String>,
    val runtime: Int,
    val episodeType: String,
    //
    val watched: Boolean = false
)