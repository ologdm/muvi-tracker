package com.example.muvitracker.repo.kotlin

import com.example.muvitracker.repo.java.dto.search.SearchDto
import com.example.muvitracker.repo.kotlin.dto.BoxoDto
import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.repo.kotlin.dto.PopuDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//  l'interfaccia Kotlin convertita rimane identica alla versione Java fornita,

// OK

interface TraktApiK {


    @GET("movies/popular") // !!! @SonoMetadati
    fun getPopularMovies()
            : Call<List<PopuDto>>  // senza {}, funzione da implementare


    @GET("movies/boxoffice")
    fun getBoxofficeMovies()
            : Call<List<BoxoDto>>

    // Path mobile
    // passa il paramentro in {}
    @GET("movies/{movie_id}?extended=full")
    fun getDetailsOfDto(@Path("movie_id") traktMovieId: Int)
            : Call<DetaDto>
    // passo parametro a -> GET {"..."}



    // TODO - modif Dto
    // Query mobile
    // - dopo diventa search/movie,show?query=stringQuery
    // - GET(""+ @Query) ---> "url_base" + " "
    @GET("search/movie,show")
    fun getSearch(@Query("query") searchString: String) // query=searchString
            : Call<SearchDto>


}