package com.example.muvitracker.domain.model.base

import com.example.muvitracker.data.dto._support.Ids

data class ShowBase(
    val title: String,
    val year: Int,
    val ids: Ids
){
    fun imageUrl(): String =
        "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
}
