package com.example.muvitracker.data.images

import javax.inject.Inject
import javax.inject.Singleton


// poster - aspect_ratio": 0.667 average
// back


@Singleton
class TmdbRepository @Inject constructor(
    private val tmdbApi: TMDbApi
) {
    private val tmdbLink = "https://image.tmdb.org/t/p/original"

    suspend fun getMoviePoster(id: Int): String {
        val posterList = tmdbApi.getMovieImages(id).posters
        val bestPoster = posterList.maxBy { it.voteAverage }  // scelgo poster migliore

        return "$tmdbLink + ${bestPoster.filePath}"
    }


    suspend fun getMovieBackdrop(id: Int): String {
        val backdropList = tmdbApi.getMovieImages(id).posters
        val bestBackdrop = backdropList.maxBy { it.voteAverage }  // scelgo poster migliore

        return "$tmdbLink + ${bestBackdrop.filePath}"
    }




}


