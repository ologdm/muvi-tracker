package com.example.muvitracker.inkotlin.model.dto.search.principali

import com.example.muvitracker.inkotlin.model.dto.search.secondarie.Ids

// OK
data class Movie(

    val title: String,
    val year: Int,
    val ids: Ids // classe K

) {

    // OK
    fun imageUrl(): String = "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"

}