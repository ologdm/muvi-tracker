package com.example.domain.model.base

import com.example.domain.model.Ids


//  to Domain() -> PopuDto, BoxoDto

data class MovieBase(
    val title: String = "",
    val year: Int = -1,
    val ids: Ids,  // val default ok
)



