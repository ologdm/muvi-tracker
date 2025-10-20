package com.example.muvitracker.data

import com.example.muvitracker.BuildConfig
import com.example.muvitracker.data.dto.episode.EpisodeDtoTmdb
import com.example.muvitracker.data.dto.movie.detail.DetailMovieDtoTmdb
import com.example.muvitracker.data.dto.person.PersonDtoTmdb
import com.example.muvitracker.data.dto.season.SeasonTmdbDto
import com.example.muvitracker.data.dto.show.detail.DetailShowTmdbDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// esempio: movie=deadpool
// dto - https://api.themoviedb.org/3/movie/293660?api_key=36b68580564c93f78a52fc28c15c44e5
// dto images - https://api.themoviedb.org/3/movie/293660/images?api_key=36b68580564c93f78a52fc28c15c44e5


interface TmdbApi {

    // TODO: BuildConfig prende valore da local propreties
    companion object {
        const val API_KEY_QUERY_MOD = BuildConfig.TMDB_API_KEY

        //        const val LANGUAGE_SYSTEM = "language=${"it-IT"}"
        var systemLanguage = LanguageManager.getSystemLocaleTag()
    }


    // DIRECT CALLS - WITH CUSTOM GLIDE -----------------------------------------------------

    // TODO: 1.1.3 chiamate per dto classico, test OK
    // NEW OK
    // filtro language
    // add all videos, con filtro lingua
    @GET("movie/{movie_id}")
    suspend fun getMovieDto(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
        @Query("language") language: String = systemLanguage,
//        @Query("language") language: String = "it-IT",
        @Query("append_to_response") appendToResponse: String = "videos",
    ): DetailMovieDtoTmdb


    // test 1399 games of thrones
    // + tutte le stagioni
    @GET("tv/{show_id}")
    suspend fun getShowDto(
        @Path("show_id") showId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
        @Query("language") language: String = systemLanguage,
        @Query("append_to_response") appendToResponse: String = "videos",
    ): DetailShowTmdbDto


    // + tutti gli episodi
    @GET("tv/{series_id}/season/{season_number}")
    suspend fun getSeasonDto(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") language: String = systemLanguage,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
    ): SeasonTmdbDto



    // serve??
    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeDto(
        @Path("series_id") seasonId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
    ): EpisodeDtoTmdb


    @GET("person/{person_id}")
    suspend fun getPersonDto(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
    ): PersonDtoTmdb

}


// TODO: note
// trovare lingua di sistema
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

