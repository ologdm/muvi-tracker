package com.example.muvitracker.inkotlin.mainactivity.base.base_repo

import com.example.muvitracker.inkotlin.repo.dto.BoxoDto
import com.example.muvitracker.inkotlin.repo.dto.Ids
import com.example.muvitracker.inkotlin.repo.dto.PopuDto

// 1. implemento movie model
// 2. funzioni estese .toDomain() per PopuDto e BoxoDto


// Classe business logic
data class MovieModel(
    val title: String,
    val year: Int,
    val ids: Ids,
) {

    fun getImageUrl(): String =
        "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"


}


// FUNZIONI estese
// OK
fun PopuDto.toDomain(dto: PopuDto): MovieModel {
    return MovieModel(dto.title, dto.year, dto.ids)
    // ritorno istanza nuova di movieModel
}


// OK
fun BoxoDto.toDomain(dto: BoxoDto): MovieModel {
    return MovieModel(
        title = dto.movie.title,
        year = dto.movie.year,
        ids = dto.movie.ids
    )
}


