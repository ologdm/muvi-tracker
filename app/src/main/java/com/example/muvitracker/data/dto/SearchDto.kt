package com.example.muvitracker.data.dto


import com.example.muvitracker.data.dto.basedto.MovieDto
import com.example.muvitracker.data.dto.basedto.ShowDto
import com.example.muvitracker.data.dto.basedto.toDomain
import com.example.muvitracker.domain.model.SearchResult

// can be: movie || show || episode(excluded)

data class SearchDto(
    val type: String,
    val score: Double,

    val movie: MovieDto?,
    val show: ShowDto?,
//    val episode: EpisodeDto?
)


fun SearchDto.toDomain(): SearchResult {
    return when (type) {
        "movie" ->
            SearchResult.MovieItem(
                movie = movie!!.toDomain(),
                score = score
            )

        "show" ->
            SearchResult.ShowItem(
                show = show!!.toDomain(),
                score = score
            )

//        "episode" ->
//            SearchResult.EpisodeItem(
//                episode = episode!!.toDomain(),
//                score = score
//            )

        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
