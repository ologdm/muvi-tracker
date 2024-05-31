package com.example.muvitracker.inkotlin.domain.model

import com.example.muvitracker.inkotlin.data.dto.support.Ids

/*
 *  to Domain()
 *       PopuDto
 *       BoxoDto
 */


data class MovieModel(
    val title: String,
    val year: Int,
    val ids: Ids,
) {
    fun getImageUrl(): String =
        "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
}



