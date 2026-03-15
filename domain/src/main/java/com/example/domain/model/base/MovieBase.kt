package com.example.domain.model.base

import com.example.domain.model.IdsDomain


//  to Domain() -> PopuDto, BoxoDto

data class MovieBase(
    val title: String = "",
    val year: Int = -1,
    val ids: IdsDomain,  // val default ok
)



