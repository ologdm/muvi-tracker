package com.example.muvitracker.data.dto.base

// OK
data class EpisodeDto(

    val season: Int,
    val number: Int,
    val title: String,
    val ids: Ids,
    val show: ShowDto

) {

    // OK uguale altre
    fun imageUrl(): String = "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"

}