package com.example.muvitracker.domain.model.base

import com.example.muvitracker.data.dto._support.Ids

data class ShowBase(
    val title: String = "",
    val year: Int = -1,
    val ids: Ids
)
