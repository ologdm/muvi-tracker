package com.example.muvitracker.data.images

import javax.inject.Inject
import javax.inject.Singleton


// poster - aspect_ratio": 0.667 average
// backdrop - 1.778, 

// TODO
//  caching link o intero json chiamata
// TODO save tmdb in detail entity
// evitare 4k


@Singleton
class TmdbRepository @Inject constructor(
    private val tmdbApi: TMDbApi
) {
    private val tmdbLink = "https://image.tmdb.org/t/p/original"


    suspend fun getMovieImages(id: Int): Map<String, String> {
        try {
            val dto = tmdbApi.getMovieImages(id) // Single network call
            val bestBackdrop = dto.backdrops.maxByOrNull { it.voteCount }?.filePath
            val bestPoster = dto.posters.maxByOrNull { it.voteCount }?.filePath
            return mapOf(
                BACKDROP_KEY to "$tmdbLink$bestBackdrop",
                POSTER_KEY to "$tmdbLink$bestPoster"
            )
        } catch (ex: Throwable) {
            ex.printStackTrace()
            return emptyMap()
        }
    }


    // chiamata unica
    suspend fun getShowImages(id: Int): Map<String, String> {
        try {
            val dto = tmdbApi.getShowImages(id) // Single network call
            val bestBackdrop = dto.backdrops.maxByOrNull { it.voteCount }?.filePath
            val bestPoster = dto.posters.maxByOrNull { it.voteCount }?.filePath
            return mapOf(
                BACKDROP_KEY to "$tmdbLink$bestBackdrop",
                POSTER_KEY to "$tmdbLink$bestPoster"
            )
        } catch (ex: Throwable) {
            ex.printStackTrace()
            return emptyMap()
        }
        // TODO save tmdb in detail entity
    }


    companion object {
        const val BACKDROP_KEY = "backdrop_key"
        const val POSTER_KEY = "poster_key"
    }
}





