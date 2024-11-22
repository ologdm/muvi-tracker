package com.example.muvitracker.data.dto.person

import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.domain.model.base.Person

data class PersonBaseDto(
    val name: String?,
    val ids: Ids
)

fun PersonBaseDto.toDomain(): Person {
    return Person(
        name = name ?: "N/A",
        ids = ids
    )
}
