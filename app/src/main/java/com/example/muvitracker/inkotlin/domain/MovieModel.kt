package com.example.muvitracker.inkotlin.domain

import com.example.muvitracker.inkotlin.data.dto.base.BoxoDto
import com.example.muvitracker.inkotlin.data.dto.base.PopuDto
import com.example.muvitracker.inkotlin.data.dto.support.Ids

/**
 * 1. implemento movie model
 * 2. funzioni estese .toDomain() per PopuDto e BoxoDto
 */


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

// OK funzione per lista

//fun List<PopuDto>.toDomain(): List<MovieModel> {
//    val listaModel = mutableListOf<MovieModel>()
//    this.forEach {
//        listaModel.add(MovieModel(it.title, it.year, it.ids))
//    }
//    return listaModel
//}
//
//
//fun List<BoxoDto>.toDomainn(): List<MovieModel> {
//    val listaModel = mutableListOf<MovieModel>()
//
//    this.forEach {
//        listaModel.add(MovieModel(it.movie.title, it.movie.year, it.movie.ids))
//    }
//    return listaModel
//}


//// singolo elemento -old
//
//fun PopuDto.toDomain(): MovieModel {
//    return MovieModel(this.title, this.year, this.ids)
//    // ritorno istanza nuova di movieModel
//}
//
//
//fun BoxoDto.toDomain(): MovieModel {
//    return MovieModel(this.movie.title, this.movie.year, this.movie.ids)
//    // ritorno istanza nuova di movieModel
//}

