package com.example.muvitracker.data.imagetmdb

import com.example.muvitracker.data.imagetmdb.dto.EpisodeImageDto
import com.example.muvitracker.data.imagetmdb.dto.MediaItem
import com.example.muvitracker.data.imagetmdb.dto.MovieShowImagesDto
import com.example.muvitracker.data.imagetmdb.dto.PersonImageDto
import com.example.muvitracker.data.imagetmdb.dto.SeasonImageDto
import com.example.muvitracker.data.imagetmdb.dto.TestTmdbDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

// esempio: movie=deadpool
//      https://api.themoviedb.org/3/movie/293660?api_key=36b68580564c93f78a52fc28c15c44e5
//      https://api.themoviedb.org/3/movie/293660/images?api_key=36b68580564c93f78a52fc28c15c44e5

// movie ok, show ok, season ok, episode ok, person ok

interface TmdbApi {

    companion object {
        const val API_KEY_QUERY = "api_key=36b68580564c93f78a52fc28c15c44e5"
    }

    // movie - https://api.themoviedb.org/3/movie/{movie_id}/images
    @GET("movie/{movie_id}/images?$API_KEY_QUERY")
    suspend fun getMovieImages(@Path("movie_id") movieId: Int): MovieShowImagesDto


    // show - https://api.themoviedb.org/3/tv/{series_id}/images
    @GET("tv/{series_id}/images?$API_KEY_QUERY")
    suspend fun getShowImages(@Path("series_id") seriesId: Int): MovieShowImagesDto


    // season - https://api.themoviedb.org/3/tv/{series_id}/season/{season_number}/images
    @GET("tv/{series_id}/season/{season_number}/images?$API_KEY_QUERY")
    suspend fun getSeasonImages(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
    ): SeasonImageDto

    // episode - https://api.themoviedb.org/3/tv/{series_id}/season/{season_number}/episode/{episode_number}/images
    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}/images?$API_KEY_QUERY")
    suspend fun getEpisodeImages(
        @Path("series_id") seasonId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): EpisodeImageDto


    // people - https://api.themoviedb.org/3/person/{person_id}/images
    @GET("person/{person_id}/images?$API_KEY_QUERY")
    suspend fun getPersonImages(
        @Path("person_id") personId: Int
    ): PersonImageDto


    // TODO - Quick Paths
    // https://api.themoviedb.org/3/
    @GET("movie/{movie_id}?$API_KEY_QUERY")
    suspend fun getMovieDtoTest(@Path("movie_id") movieId: Int) : TestTmdbDto

    @GET("tv/{show_id}?$API_KEY_QUERY")
    suspend fun getShowDtoTest(@Path("show_id") showId: Int) : TestTmdbDto


}


// DAGGER
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun getApi(): TmdbApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(TmdbApi::class.java)
    }
}

