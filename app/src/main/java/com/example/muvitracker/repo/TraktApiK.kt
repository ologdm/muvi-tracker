package com.example.muvitracker.repo

import com.example.muvitracker.repo.dto.search.SearchDto
import com.example.muvitracker.repository.dto.BoxofficeDtoK
import com.example.muvitracker.repository.dto.DetailsDtoK
import com.example.muvitracker.repository.dto.PopularDtoK
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//  l'interfaccia Kotlin convertita rimane identica alla versione Java fornita,

// OK

interface TraktApiK {


    @GET("movies/popular") // !!! @SonoMetadati
    fun getPopularMovies()
            : Call<List<PopularDtoK>>  // senza {}, funzione da implementare


    @GET("moovies/boxoffice")
    fun getBoxoffice()
            : Call<List<BoxofficeDtoK>>

    // Path mobile
    // passa il paramentro in {}
    @GET("movies/{movie_id}?extended=full")
    fun getDetailsDto(@Path("movie_id") movieId: Int)
            : Call<DetailsDtoK>
    // passo parametro a -> GET {"..."}



    // TODO - modif Dto
    // Query mobile
    // - dopo diventa search/movie,show?query=stringQuery
    // - GET(""+ @Query) ---> "url_base" + " "
    @GET("search/movie,show")
    fun getSearch(@Query("query") searchString: String) // query=searchString
            : Call<SearchDto>


}