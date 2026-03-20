package com.example.domain.model


data class Person(
    val name: String?,
    val ids: Ids,
    val biography: String?,
    val birthday: String?,
    val death: String?,
    val age : Int?, // calculated value, -1 or age
    val birthplace: String?,
    val knownForDepartment: String?,

    // social platforms links
    val twitter: String?,
    val facebook: String?,
    val instagram: String?,
    val wikipedia: String?
)
