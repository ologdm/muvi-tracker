package com.example.muvitracker.data.dto.basedto

import com.example.muvitracker.domain.model.base.Show


data class ShowBaseDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun ShowBaseDto.toDomain(): Show {
    return Show(
        title = this.title,
        year = this.year,
        ids = this.ids
    )
}