package com.example.muvitracker.data

import com.example.muvitracker.data.dto.BoxoDto
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.data.dto.basedto.MovieDto
import com.example.muvitracker.data.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TraktApi {

    @GET("movies/popular")
    suspend fun getPopularMovies()
            : List<MovieDto>

    @GET("movies/boxoffice")
    suspend fun getBoxoMovies()
            : List<BoxoDto>

    @GET("movies/{movie_id}?extended=full")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int)
            : DetailDto

    @GET("search/movie")  // with mobile query
    suspend fun getSearch(@Query("query") searchString: String)
            : List<SearchDto>

}
