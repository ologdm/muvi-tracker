package com.example.data.dto.person

import android.annotation.SuppressLint
import com.example.data.dto._support.Ids
import com.example.data.dto._support.toDomain
import com.example.domain.model.base.PersonBase
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PersonBaseDto(
    val name: String = "",
    val ids: Ids
)

fun PersonBaseDto.toDomain(): PersonBase {
    return PersonBase(
        name = name ?: "",
        ids = ids.toDomain()
    )
}
