package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.utilsdto.Ids

//data class EpisodeExtended (
//    val episodeTraktId: Int,
//    val seasonNumber: Int,
//    val episodeNumber: Int,
//    val title: String,
//    val ids: Ids,
//    val showId: Int,
//    //
//    val numberAbs: Int,
//    val overview: String,
//    val rating: String,
//    val firstAiredFormatted: String?,
//    val availableTranslations: List<String>,
//    val runtime: Int,
//    val episodeType: String,
//    //
//    val watched: Boolean = false
//)

// TODO: OK 1.1.3
data class EpisodeExtended (
    val episodeTraktId: Int,
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val numberAbs: Int?,
    val ids: Ids,
    val showId: Int,
    //
    val title: String?,
    val overview: String?,
    val firstAiredFormatted: String?,
    val runtime: Int?,
    val episodeType: String?, // premiere, standard, final
    val traktRating: String?,
    //
    val watched: Boolean
)