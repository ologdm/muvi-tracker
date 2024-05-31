package com.example.muvitracker.inkotlin.data.dto.search.Internal

import com.example.muvitracker.inkotlin.data.dto.support.Ids

// OK
data class Episode(

    val season: Int,
    val number: Int,
    val title: String,
    val ids: Ids,
    val show: Show

) {

    // OK uguale altre
    fun imageUrl(): String = "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"

}