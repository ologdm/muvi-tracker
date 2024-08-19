package com.example.muvitracker.data.dto.basedto

import com.example.muvitracker.domain.model.base.Episode


data class EpisodeBaseDto(
    val season: Int,
    val number: Int,
    val title: String,
    val ids: Ids,
    val show: ShowBaseDto
)

fun EpisodeBaseDto.toDomain(): Episode {
    return Episode(
        season = season,
        number = number,
        title = title,
        ids = ids,
        show = show.toDomain()
    )
}