package com.example.domain.model

// 1.1.3 OK
data class Episode (
    val episodeTraktId: Int,
    val seasonNumber: Int, // default -1 su entity
    val episodeNumber: Int, // default -1 su entity
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