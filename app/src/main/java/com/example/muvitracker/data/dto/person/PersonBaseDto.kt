package com.example.muvitracker.data.dto.person

import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.base.PersonBase

data class PersonBaseDto(
    val name: String?,
    val ids: Ids
)

fun PersonBaseDto.toDomain(): PersonBase {
    return PersonBase(
        name = name ?: "N/A",
        ids = ids
    )
}
