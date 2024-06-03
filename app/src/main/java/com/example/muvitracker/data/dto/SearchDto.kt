package com.example.muvitracker.data.dto


import com.example.muvitracker.data.dto.base.EpisodeDto
import com.example.muvitracker.data.dto.base.MovieDto
import com.example.muvitracker.data.dto.base.ShowDto

// can be: movie || show || episode

data class SearchDto(
    val type: String,
    val score: Double,

    val movie: MovieDto?, // == popular
    val show: ShowDto?, // K
    val episode: EpisodeDto? // K
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