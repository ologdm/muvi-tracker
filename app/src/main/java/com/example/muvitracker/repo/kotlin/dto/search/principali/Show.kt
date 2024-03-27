package com.example.muvitracker.repo.kotlin.dto.search.principali

import com.example.muvitracker.repo.kotlin.dto.search.secondarie.Ids

// OK
data class Show(

    val title: String,
    val year: Int,
    val ids: Ids // classe K
) {

    // OK
    fun imageUrl(): String = "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
}