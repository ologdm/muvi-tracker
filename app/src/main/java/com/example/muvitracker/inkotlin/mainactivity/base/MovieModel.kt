package com.example.muvitracker.inkotlin.mainactivity.base

import com.example.muvitracker.inkotlin.repo.dto.base.BoxoDto
import com.example.muvitracker.inkotlin.repo.dto.base.PopuDto
import com.example.muvitracker.inkotlin.repo.dto.support.Ids

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


// OK funzione per lista
fun List<PopuDto>.toDomain ():List<MovieModel>{
    val listaModel = mutableListOf<MovieModel>()
    this.forEach {
         listaModel.add(MovieModel(it.title,it.year,it.ids))
    }
    return listaModel
}


// OK

fun List<BoxoDto>.toDomainn(): List<MovieModel> {
    val listaModel = mutableListOf<MovieModel>()

    this.forEach {
        listaModel.add(MovieModel(it.movie.title,it.movie.year,it.movie.ids))
    }
    return listaModel
}




// OK
/*
fun PopuDto.toDomain(): MovieModel {
    return MovieModel(this.title, this.year, this.ids)
    // ritorno istanza nuova di movieModel
}
 */
