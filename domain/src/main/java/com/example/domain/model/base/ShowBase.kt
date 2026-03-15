package com.example.domain.model.base

import com.example.domain.model.Ids

data class ShowBase(
    val title: String = "",
    val year: Int = -1,
    val ids: Ids
)
