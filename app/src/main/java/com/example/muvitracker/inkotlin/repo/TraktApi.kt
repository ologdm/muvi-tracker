package com.example.muvitracker.inkotlin.repo

import com.example.muvitracker.inkotlin.repo.dto.base.BoxoDto
import com.example.muvitracker.inkotlin.repo.dto.DetaDto
import com.example.muvitracker.inkotlin.repo.dto.base.PopuDto
import com.example.muvitracker.inkotlin.repo.dto.search.SearDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//  l'interfaccia Kotlin convertita rimane identica alla versione Java fornita,

interface TraktApi {

    // 1.
    @GET("movies/popular") // !!! @SonoMetadati
    fun getPopularMovies()
            : Call<List<PopuDto>>

    // 2.
    @GET("movies/boxoffice")
    fun getBoxofficeMovies()
            : Call<List<BoxoDto>>

    // 3. Path mobile - passa il paramentro in {}
    @GET("movies/{movie_id}?extended=full")
    fun getDetailsOfDto(@Path("movie_id") traktMovieId: Int)
            : Call<DetaDto>
    // passo parametro a -> GET {"..."}


    /* 4. Query mobile
    // - dopo diventa search/movie,show?query=stringQuery
    // - GET(""+ @Query) ---> "url_base" + " "  */
    @GET("search/movie")
    fun getSearch(@Query("query") searchString: String) // query=searchString
            : Call<List<SearDto>>
} // @GET("search/movie,shows,episodes") -> altre tipologie - dto completo in java
