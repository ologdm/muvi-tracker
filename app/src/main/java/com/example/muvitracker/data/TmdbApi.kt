package com.example.muvitracker.data


import com.example.muvitracker.BuildConfig
import com.example.muvitracker.data.dto.episode.EpisodeTmdbDto
import com.example.muvitracker.data.dto.movie.detail.MovieTmdbDto
import com.example.muvitracker.data.dto.person.detail.PersonTmdbDto
import com.example.muvitracker.data.dto.provider.MovieProvidersResponseDto
import com.example.muvitracker.data.dto.season.SeasonEpTmdbDto
import com.example.muvitracker.data.dto.show.detail.ShowTmdbDto
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
    }


    /**
     * RELEASE 1.1.3 - Update DTO language when the system language changes.
     *  NOTES:
     *      - The language cannot be stored as a variable and passed to the function, as it would not be dynamic.
     *      - The language cannot be passed as a `val get()` to the function.
     *      - You must pass the function call directly inside the Retrofit method parameters.
     */


    @GET("movie/{movie_id}")
    suspend fun getMovieDto(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
        @Query("language") language: String = LanguageManager.getAppLocaleLanguageTag(),
        @Query("append_to_response") appendToResponse: String = "videos",
    ): MovieTmdbDto

    // 1.1.3 OK
    // test 1399 games of thrones
    // + tutte le stagioni
    @GET("tv/{show_id}")
    suspend fun getShowDto(
        @Path("show_id") showId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
        @Query("language") language: String = LanguageManager.getAppLocaleLanguageTag(),
        @Query("append_to_response") appendToResponse: String = "videos",
    ): ShowTmdbDto


    // TODO 1.1.3 - OK
    // + tutti gli episodi
    @GET("tv/{series_id}/season/{season_number}")
    suspend fun getSeasonDto(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") language: String = LanguageManager.getAppLocaleLanguageTag(),
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
    ): SeasonEpTmdbDto // dto corretto - SeasonEpTmdbDto


    // TODO 1.1.3 ??? - serve?? solo su fetcher, modificare
    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeDto(
        @Path("series_id") seasonId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
    ): EpisodeTmdbDto


    //
    @GET("person/{person_id}")
    suspend fun getPersonDto(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD,
    ): PersonTmdbDto


    /** RELEASE 1.2.0 - Providers -----------------------------------------------------------
     *  NOTES:
     *
     *
     */

    // 1.2.0 providers OK
    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieProviders(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD
    ): MovieProvidersResponseDto


    @GET("tv/{series_id}/watch/providers")
    suspend fun getShowProviders(
        @Path("series_id") showId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD
    ): MovieProvidersResponseDto

}



// -------------------NOTES -----------------------------------------
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

