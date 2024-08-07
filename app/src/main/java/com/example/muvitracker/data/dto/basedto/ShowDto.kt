package com.example.muvitracker.data.dto.basedto

import com.example.muvitracker.domain.model.base.Show


data class ShowDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun ShowDto.toDomain(): Show {
    return Show(
        title = this.title,
        year = this.year,
        ids = this.ids
    )
}