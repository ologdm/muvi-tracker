package com.example.muvitracker.data

import android.annotation.SuppressLint
import com.example.muvitracker.BuildConfig
import com.example.muvitracker.data.dto.episode.EpisodeTmdbDto
import com.example.muvitracker.data.dto.movie.detail.MovieTmdbDto
import com.example.muvitracker.data.dto.person.detail.PersonTmdbDto
import com.example.muvitracker.data.dto.season.SeasonEpTmdbDto
import com.example.muvitracker.data.dto.show.detail.ShowTmdbDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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


    /** RELEASE 1.1.4 - Providers -----------------------------------------------------------
     *  NOTES:
     *
     *
     */


    // TODO: 1.1.4 providers
    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieProviders(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD // api key mia
    ): MovieWatchProvidersResponseDto


    @GET("tv/{series_id}/watch/providers")
    suspend fun getShowProviders(
        @Path("series_id") showId: Int,
        @Query("api_key") apiKey: String = API_KEY_QUERY_MOD // api key mia
    ): MovieWatchProvidersResponseDto

}


// TODO: 1.1.4 - providers
// response tipe:
// 1. buy -  acquista
// 2. rent - noleggia
// 3. flatrate - streaming abbonamento
// 4. free - Film disponibile gratuitamente, senza costi di acquisto o abbonamento.
// 5. ads - Film disponibile gratuitamente ma con pubblicità.
// link - link per tmdb alla sezione providers


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MovieWatchProvidersResponseDto(
    val id: Int, // movie id
    val results: Map<String, RegionProvidersDto> = emptyMap() // "AO" -> RegionProviders
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
class RegionProvidersDto(
    // 1. link generale
    val link: String?,

    // 2. una delle 5 opzioni:
    val buy: List<ProviderDto> = emptyList(), // acquisto
    val flatrate: List<ProviderDto> = emptyList(), // streaming con abbonnamewnto
    val rent: List<ProviderDto> = emptyList(), // noleggio
    val free: List<ProviderDto> = emptyList(), // acquisto
    val ads: List<ProviderDto> = emptyList() // acquisto
)


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ProviderDto(
    @SerialName("provider_id") val providerId: Int,
    @SerialName("provider_name") val providerName: String?,
    @SerialName("logo_path") val logoPath: String?,
    @SerialName("display_priority") val displayPriority: Int?, //,1,2,3
)


/*
ignoreUnknownKeys = true →
        se il JSON restituito dall’API ha campi extra che non sono definiti nei tuoi data class,
        li ignora invece di lanciare un errore.

isLenient = true →
        permette al parser di essere più permissivo su JSON “non standard”
        (ad esempio virgolette mancanti o valori nullabili).

encodeDefaults = true →  TODO importante !!!!!
        quando serializzi oggetti, include anche i valori di default delle proprietà.

coerceInputValues = true → TODO importante !!!!!
        se un campo del JSON è null ma la tua data class ha un valore di default,
        lo userà automaticamente invece di fallire.
 */


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

