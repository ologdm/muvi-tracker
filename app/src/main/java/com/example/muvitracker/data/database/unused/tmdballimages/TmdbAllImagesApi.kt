//package com.example.muvitracker.data.database.all_images_tmdb
//
//import com.example.muvitracker.data.TmdbApi.Companion.API_KEY_QUERY_MOD
//import com.example.muvitracker.data.dto.tmdb.old.EpisodeImageDto
//import com.example.muvitracker.data.dto.tmdb.old.MovieShowImagesDto
//import com.example.muvitracker.data.dto.tmdb.old.PersonImageDto
//import com.example.muvitracker.data.dto.tmdb.old.SeasonImageDto
//import retrofit2.http.GET
//import retrofit2.http.Path
//import retrofit2.http.Query
//
//interface TmdbAllImagesApi {
//
//    // REPOSITORY CALLS - all images for item  ----------------------------------------------------------
//    // non utilizzate
//    // movie - https://api.themoviedb.org/3/movie/{movie_id}/images
//    @GET("movie/{movie_id}/images")
//    suspend fun getMovieAllImages(
//        @Path("movie_id") movieId: Int,
//        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
//    ): MovieShowImagesDto
//
//
//    // show - https://api.themoviedb.org/3/tv/{series_id}/images
//    @GET("tv/{series_id}/images")
//    suspend fun getShowAllImages(
//        @Path("series_id") seriesId: Int,
//        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
//    ): MovieShowImagesDto
//
//
//    // season - https://api.themoviedb.org/3/tv/{series_id}/season/{season_number}/images
//    @GET("tv/{series_id}/season/{season_number}/images")
//    suspend fun getSeasonAllImages(
//        @Path("series_id") seriesId: Int,
//        @Path("season_number") seasonNumber: Int,
//        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
//    ): SeasonImageDto
//
//    // episode - https://api.themoviedb.org/3/tv/{series_id}/season/{season_number}/episode/{episode_number}/images
//    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}/images")
//    suspend fun getEpisodeAllImages(
//        @Path("series_id") seasonId: Int,
//        @Path("season_number") seasonNumber: Int,
//        @Path("episode_number") episodeNumber: Int,
//        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
//    ): EpisodeImageDto
//
//
//    // people - https://api.themoviedb.org/3/person/{person_id}/images
//    @GET("person/{person_id}/images")
//    suspend fun getPersonAllImages(
//        @Path("person_id") personId: Int,
//        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
//    ): PersonImageDto
//}