package com.example.muvitracker.data

import com.example.muvitracker.BuildConfig
import com.example.muvitracker.data.dto.tmdb.EpisodeImageDto
import com.example.muvitracker.data.dto.tmdb.MovieShowImagesDto
import com.example.muvitracker.data.dto.tmdb.PersonImageDto
import com.example.muvitracker.data.dto.tmdb.SeasonImageDto
import com.example.muvitracker.data.dto.tmdb.TmdbEpisodeDto
import com.example.muvitracker.data.dto.tmdb.TmdbMovieDto
import com.example.muvitracker.data.dto.tmdb.TmdbPersonDto
import com.example.muvitracker.data.dto.tmdb.TmdbSeasonDto
import com.example.muvitracker.data.dto.tmdb.TmdbShowDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

// esempio: movie=deadpool
// dto - https://api.themoviedb.org/3/movie/293660?api_key=36b68580564c93f78a52fc28c15c44e5
// dto images - https://api.themoviedb.org/3/movie/293660/images?api_key=36b68580564c93f78a52fc28c15c44e5


interface TmdbApi {

    // TODO: BuildConfig prende valore da local propreties
    companion object {
        const val API_KEY_QUERY = "api_key=${BuildConfig.TMDB_API_KEY}"
        const val API_KEY_QUERY_MOD = BuildConfig.TMDB_API_KEY

        //        const val LANGUAGE_SYSTEM = "language=${"it-IT"}"
        var systemLanguage = Locale.getDefault().toLanguageTag() // formato per tmdb
    }


    // DIRECT CALLS - WITH CUSTOM GLIDE -----------------------------------------------------

    // TODO: 1.1.3 chiamate per dto classico, test OK
    // old
//    @GET("movie/{movie_id}?$API_KEY_QUERY")
//    suspend fun getMovieDto(
//        @Path("movie_id") movieId: Int
//    ): TmdbMovieDto

    // new
    @GET("movie/{movie_id}")
    suspend fun getMovieDto(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
//        @Query("language") language :String = systemLanguage
        @Query("language") language: String = "it-IT",
//        @Query("videos") language: String = "it-IT",
    ): TmdbMovieDto

    // TODO: note
    // GPT - trovare lingua di sistema
//    val locale = Resources.getSystem().configuration.locales.get(0)
//    val locale = Locale.getDefault()
    // nome abbrev
//    val language = locale.language       // es: "it"
    // nome completo lingua
//    val languageName = locale.displayLanguage // es: "italiano"

//    val country = locale.country         // es: "IT"

//    language restituisce il codice della lingua (ISO 639-1), es. "en", "it".
//    country restituisce il codice del paese (ISO 3166-1), es. "US", "IT".

    // tmdb usa ISO 639-1 come il sistema operativo

    // trailer tradotto -> comporre link
//    https://api.themoviedb.org/3/tv/{series_id}/videos?language=it-IT


    @GET("tv/{show_id}?$API_KEY_QUERY")
    suspend fun getShowDto(
        @Path("show_id") showId: Int
    ): TmdbShowDto

    @GET("tv/{series_id}/season/{season_number}?$API_KEY_QUERY")
    suspend fun getSeasonDto(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
    ): TmdbSeasonDto

    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}?$API_KEY_QUERY")
    suspend fun getEpisodeDto(
        @Path("series_id") seasonId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): TmdbEpisodeDto


    @GET("person/{person_id}?$API_KEY_QUERY")
    suspend fun getPersonDto(
        @Path("person_id") personId: Int
    ): TmdbPersonDto





    // REPOSITORY CALLS - all images for item  ----------------------------------------------------------
    // non utilizzate

    // movie - https://api.themoviedb.org/3/movie/{movie_id}/images
    @GET("movie/{movie_id}/images?$API_KEY_QUERY")
    suspend fun getMovieAllImages(@Path("movie_id") movieId: Int): MovieShowImagesDto


    // show - https://api.themoviedb.org/3/tv/{series_id}/images
    @GET("tv/{series_id}/images?$API_KEY_QUERY")
    suspend fun getShowAllImages(@Path("series_id") seriesId: Int): MovieShowImagesDto


    // season - https://api.themoviedb.org/3/tv/{series_id}/season/{season_number}/images
    @GET("tv/{series_id}/season/{season_number}/images?$API_KEY_QUERY")
    suspend fun getSeasonAllImages(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
    ): SeasonImageDto

    // episode - https://api.themoviedb.org/3/tv/{series_id}/season/{season_number}/episode/{episode_number}/images
    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}/images?$API_KEY_QUERY")
    suspend fun getEpisodeAllImages(
        @Path("series_id") seasonId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): EpisodeImageDto


    // people - https://api.themoviedb.org/3/person/{person_id}/images
    @GET("person/{person_id}/images?$API_KEY_QUERY")
    suspend fun getPersonAllImages(
        @Path("person_id") personId: Int
    ): PersonImageDto

}

