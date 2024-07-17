package com.example.muvitracker.data.dto.basedto

import com.example.muvitracker.domain.model.base.Episode


data class EpisodeDto(
    val season: Int,
    val number: Int,
    val title: String,
    val ids: Ids,
    val show: ShowDto
)

fun EpisodeDto.toDomain(): Episode {
    return Episode(
        season = season,
        number = number,
        title = title,
        ids = ids,
        show = show.toDomain()
    )
}