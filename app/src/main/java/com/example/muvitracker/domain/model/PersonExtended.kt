package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.utilsdto.Ids


data class PersonExtended(
    val name: String,
    val ids: Ids,
    val biography: String,
    val birthday: String,
    val death: String,
    val age :String, // calculated value
    val birthplace: String,
    val knownForDepartment: String,

    // social platforms links
    val twitter: String,
    val facebook: String,
    val instagram: String,
    val wikipedia: String
)
