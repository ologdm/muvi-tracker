package com.example.muvitracker.domain.model

import com.example.muvitracker.data.dto.base.Ids

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
    fun imageUrl(): String =
        "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
}



