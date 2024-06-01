package com.example.muvitracker.inkotlin.data

import com.example.muvitracker.inkotlin.data.dto.BoxoDto
import com.example.muvitracker.inkotlin.data.dto.DetailDto
import com.example.muvitracker.inkotlin.data.dto.base.MovieDto
import com.example.muvitracker.inkotlin.data.dto.SearchDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TraktApi {

    @GET("movies/popular")
    fun getPopularMovies()
            : Call<List<MovieDto>>


    @GET("movies/boxoffice")
    fun getBoxofficeMovies()
            : Call<List<BoxoDto>>


    @GET("movies/{movie_id}?extended=full")  // con path mobile
    fun getMovieDetails(@Path("movie_id") movieId: Int)
            : Call<DetailDto>


    @GET("search/movie")  // con query mobile
    fun getSearch(@Query("query") searchString: String)
            : Call<List<SearchDto>>

}
