package com.example.muvitracker.inkotlin.data.dto.search


import com.example.muvitracker.inkotlin.data.dto.MovieDto
import com.example.muvitracker.inkotlin.data.dto.search.Internal.EpisodeDto
import com.example.muvitracker.inkotlin.data.dto.search.Internal.ShowDto

// can be: movie || show || episode

data class SearDto(
    val type: String,
    val score: Double,

    val movieDto: MovieDto?, // == popular
    val showDto: ShowDto?, // K
    val episodeDto: EpisodeDto? // K
)


//
//fun SearDto.toDomain() : SearchMovieItem {
//    return SearchMovieItem(
//        type = type,
//        score = score,
//        ids =  when (type){
//            "movie" -> movieDto?.ids
//            "show" -> showDto?.ids
//            "episode" -> episodeDto?.ids
//            else -> Ids() // valore default
//        }
//    )
//}