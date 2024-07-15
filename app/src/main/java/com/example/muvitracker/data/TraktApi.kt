package com.example.muvitracker.data

import com.example.muvitracker.data.dto.BoxoDto
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.data.dto.basedto.MovieDto
import com.example.muvitracker.data.dto.SearchDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TraktApi {

    // coroutines
    @GET("movies/popular")
    suspend fun getPopularMoviesTest()
            : List<MovieDto>

    @GET("movies/boxoffice")
    suspend fun getBoxoMoviesTest()
            : List<BoxoDto>

    @GET("movies/{movie_id}?extended=full")
    suspend fun getMovieDetailTest(@Path("movie_id") movieId: Int)
            : DetailDto

    // coroutines
    @GET("search/movie")  // with mobile query
    suspend fun getSearchTest(@Query("query") searchString: String)
            : List<SearchDto>


    // calls
//    @GET("search/movie")  // with mobile query
//    fun getSearch(@Query("query") searchString: String)
//            : Call<List<SearchDto>>



}
