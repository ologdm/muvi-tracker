package com.example.muvitracker.domain.model.base

import com.example.muvitracker.data.dto._support.Ids


//  to Domain() -> PopuDto, BoxoDto

data class MovieBase(
    val title: String = "",
    val year: Int = -1,
    val ids: Ids,  // val default ok
)



