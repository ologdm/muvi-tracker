package com.example.muvitracker.data


import com.example.muvitracker.BuildConfig
import com.example.muvitracker.data.dto.OmdbResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    companion object{
        const val API_KEY_QUERY_MOD = BuildConfig.OMDB_API_KEY
    }


    @GET(".")
    suspend fun getData (
        @Query("i") imdbId : String,
        @Query("apikey") apikey : String = API_KEY_QUERY_MOD,
    ): OmdbResultDto


}