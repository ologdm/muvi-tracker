package com.example.muvitracker.inkotlin.domain.model

import com.example.muvitracker.inkotlin.data.dto.base.Ids

/*
 *  to Domain()
 *       PopuDto
 *       BoxoDto
 */


data class MovieItem(
    val title: String,
    val year: Int,
    val ids: Ids,
) {
    fun getImageUrl(): String =
        "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
}



