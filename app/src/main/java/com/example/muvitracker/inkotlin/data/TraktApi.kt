package com.example.muvitracker.inkotlin.data

import com.example.muvitracker.inkotlin.data.dto.BoxoDto
import com.example.muvitracker.inkotlin.data.dto.DetaDto
import com.example.muvitracker.inkotlin.data.dto.PopuDto
import com.example.muvitracker.inkotlin.data.dto.search.SearDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TraktApi {

    @GET("movies/popular")
    fun getPopularMovies()
            : Call<List<PopuDto>>


    @GET("movies/boxoffice")
    fun getBoxofficeMovies()
            : Call<List<BoxoDto>>


    @GET("movies/{movie_id}?extended=full")  // con path mobile
    fun getDetailsOfDto(@Path("movie_id") traktMovieId: Int)
            : Call<DetaDto>


    @GET("search/movie")  // con query mobile
    fun getSearch(@Query("query") searchString: String)
            : Call<List<SearDto>>

}
