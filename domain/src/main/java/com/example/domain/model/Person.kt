package com.example.domain.model


data class Person(
    val name: String?,
    val ids: Ids,
    val biography: String?,
    val birthday: String?,
    val death: String?,
    val age: Int?, // calculated value, -1 or age
    val birthplace: String?,
    val knownForDepartment: String?,

    // social platforms links
    val twitter: String?,
    val facebook: String?,
    val instagram: String?,
    val wikipedia: String?
)


data class PersonCredit(
    val isShow : Boolean,

    val title : String?,
    val year : Int?, // NOTE: per alcuni e -1
    val ids : Ids,
    val status : String?,
    val overview : String?,

    val character: String?,

    // solo shows
    val seriesRegular: Boolean?, // false
    val episodeCount: Int?, // 1

    // solo crew
    val job: String?,
    val jobs: List<String>?,

)

