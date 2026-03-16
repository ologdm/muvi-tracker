package com.example.data


import com.example.data.dto.OmdbResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    companion object{
//        const val API_KEY_QUERY_MOD = BuildConfig.OMDB_API_KEY
        const val API_KEY_QUERY_MOD = "BuildConfig.OMDB_API_KEY" // TODO: check dopo sync
    }


    @GET(".")
    suspend fun getData (
        @Query("i") imdbId : String,
        @Query("apikey") apikey : String = API_KEY_QUERY_MOD,
    ): OmdbResultDto


}