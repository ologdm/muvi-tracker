package com.example.domain.model

import com.example.domain.model.base.MovieBase
import com.example.domain.model.base.ShowBase


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


// TODO TEST
data class PersonCredit(
    val character: String?,
    val characters: List<String>?,
    //
    val show: ShowBase?,
    val movie: MovieBase?,
    // solo shows
    val episode_count: Int?, // 1
    val series_regular: Boolean?, // false
    // solo crew, es directing
    val job: String?, // "Assistant Director"
    val jobs: List<String>?, // ["Assistant Director", "Assistant"]

) {

    val isShow = show != null
    val isMovie = movie != null

    val year = if (isShow) show!!.year else movie!!.year
}
