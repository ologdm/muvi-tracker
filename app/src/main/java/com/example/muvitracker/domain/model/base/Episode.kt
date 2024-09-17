package com.example.muvitracker.domain.model.base

import com.example.muvitracker.data.dto.base.Ids

data class Episode(
    val season: Int,
    val number: Int,
    val title: String,
    val ids: Ids,
    val show: Show
)