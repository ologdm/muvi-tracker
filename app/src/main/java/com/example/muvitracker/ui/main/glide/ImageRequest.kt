package com.example.muvitracker.ui.main.glide

import com.example.muvitracker.data.dto.base.Ids

sealed interface ImageRequest {

    val ids: Ids

    data class Episode(
        val seasonId: Int,
        val season: Int,
        val episode: Int,
        override val ids: Ids,
    ): ImageRequest

}