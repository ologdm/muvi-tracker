package com.example.data.api

import android.annotation.SuppressLint
import com.example.data.dto.episode.EpisodeTraktDto
import com.example.data.dto.movie.MovieBaseDto
import com.example.data.dto.movie.detail.MovieTraktDto
import com.example.data.dto.movie.explore.AnticipatedDtoM
import com.example.data.dto.movie.explore.BoxofficeDtoM
import com.example.data.dto.movie.explore.FavoritedDtoM
import com.example.data.dto.movie.explore.WatchedDtoM
import com.example.data.dto.person.CastResponseDto
import com.example.data.dto.person.detail.PersonTraktDto
import com.example.data.dto.search.SearchDto
import com.example.data.dto.season.SeasonTraktDto
import com.example.data.dto.show.ShowBaseDto
import com.example.data.dto.show.detail.ShowTraktDto
import com.example.data.dto.show.explore.AnticipatedShowDto
import com.example.data.dto.show.explore.FavoritedShowDto
import com.example.data.dto.show.explore.WatchedShowDto
import com.example.domain.model.PersonCredit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TraktApi {

    companion object {
        const val SEARCH_LIMIT = "24"
    }

    // SEARCH ----------------------------------------------------------------
    // type: movie, show, person
    @GET("search/{type_filter}?limit=$SEARCH_LIMIT")
    suspend fun getSearch(
        @Path("type_filter") typeFilter: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): List<SearchDto>


    // MOVIES ----------------------------------------------------------------
    // todo filters - genre, year

    // ?page={page}&limit={limit} - impostazione standard
    @GET("movies/popular") // TODO change name - getPopularMoviesPage
    suspend fun getPopularMovies(
        @Query("page") page: Int, // standard
        @Query("limit") limit: Int // standard
    ): List<MovieBaseDto>

    @GET("movies/watched/weekly")
    suspend fun getWatchedMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<WatchedDtoM>

    @GET("movies/favorited/weekly")
    suspend fun getFavoritedMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<FavoritedDtoM>

    @GET("movies/anticipated")
    suspend fun getAnticipatedMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<AnticipatedDtoM>

    @GET("movies/boxoffice")
    suspend fun getBoxoMovies(): List<BoxofficeDtoM>


    // SHOWS -----------------------------------------------------------------------------------------
    // todo filters - gennre, year, network

    @GET("shows/popular")
    suspend fun getPopularShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<ShowBaseDto>

    @GET("shows/watched/weekly")
    suspend fun getWatchedShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<WatchedShowDto>

    @GET("shows/favorited/weekly")
    suspend fun getFavoritedShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<FavoritedShowDto>

    @GET("shows/anticipated")
    suspend fun getAnticipatedShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<AnticipatedShowDto>


    // DETAIL MOVIE/SHOW -------------------------------------------------------------------------------
    @GET("movies/{movie_id}?extended=full")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int)
            : MovieTraktDto


    //  -> seasons(1,2,3,4...n), cast, related
    // https://api.trakt.tv/shows/id
    @GET("shows/{show_id}?extended=full")
    suspend fun getShowDetail(@Path("show_id") showId: Int): ShowTraktDto


    // https://api.trakt.tv/shows/id/seasons/?extended=full
    @GET("shows/{show_id}/seasons/?extended=full")
    suspend fun getAllSeasons(@Path("show_id") showId: Int): List<SeasonTraktDto>


    // RELATED MOVIE/SHOWS --------------------------------------------------------------------------
    @GET("movies/{movie_id}/related")
    suspend fun getMovieRelatedMovies(
        @Path("movie_id") movieId: Int
    ): List<MovieBaseDto>

    @GET("shows/{show_id}/related")
    suspend fun getShowRelatedShows(
        @Path("show_id") showId: Int
    ): List<ShowBaseDto>


    // SEASON FRAGMENT ------------------------------------------------------------------------------
    // https://api.trakt.tv/shows/game-of-thrones/seasons/number?extended=full
    // season info
    @GET("shows/{show_id}/seasons/{season_number}/info?extended=full")
    suspend fun getSeasonInfo(
        @Path("show_id") showId: Int,
        @Path("season_number") seasonNumber: Int
    ): SeasonTraktDto


    // es - https://api.themoviedb.org/3/tv/1399/season/1
    // all episodes - già fornisce un dto ccon tutti gli episodi
    @GET("shows/{show_id}/seasons/{season_number}?extended=full")
    suspend fun getSeasonWithEpisodes(
        @Path("show_id") showId: Int,
        @Path("season_number") seasonNumber: Int
    ): List<EpisodeTraktDto>


    // (single episode) - non usato
//    @GET("shows/{show_id}/seasons/{season_number}/episodes/{episode_number}?extended=full")
//    suspend fun getEpisodeInfo(
//        @Path("show_id") showId: Int,
//        @Path("season_number") seasonNumber: Int,
//        @Path("episode_number") episodeNumber: Int
//    ): EpisodeExtenDto


    // CAST, PERSON DETAIL ------------------------------------------------------------------------------------

    // https://api.trakt.tv/movies/id/people
    @GET("movies/{movie_id}/people")
    suspend fun getAllMovieCast(
        @Path("movie_id") movieId: Int
    ): CastResponseDto

    // https://api.trakt.tv/shows/id/people
    @GET("shows/{show_id}/people")
    suspend fun getAllShowCast(
        @Path("show_id") showId: Int
    ): CastResponseDto


    // https://api.trakt.tv/people/id?extended=full
    // bryan-cranston
    @GET("people/{person_id}?extended=full")
    suspend fun getPersonDetail(
        @Path("person_id") personId: Int
    ): PersonTraktDto


    @GET("people/{traktId}/{type}?extended=full") // extended=full serve
    suspend fun getPersonCredits(
        @Path("traktId") traktId: Int,
        @Path("type") type: String,
    ): TraktCreditsResponseDto

}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TraktCreditsResponseDto(
    val cast: List<TraktCreditDto>? = null,
//    val crew: Map<String, List<PersonCreditDto>>? = null, // TODO check gestione
)


// NOTE: null tutti i campi che possono non esserci
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TraktCreditDto(
    val character: String? = null,
//    val characters: List<String>? = null,
    //
    val show: ShowTraktDto? = null,
    val movie: MovieTraktDto? = null,
    // solo shows
    @SerialName("episode_count") val episodeCount: Int? = null, // 1
    @SerialName("series_regular")val seriesRegular: Boolean? = null, // false
    // solo crew, es directing
    val job: String? = null, // "Assistant Director"
    val jobs: List<String>? = null, // ["Assistant Director", "Assistant"]

    // TODO: 3.6.26 - dati aggiuntivi:
    //  movie -> released
    //  show -> first_aired
    val released : Int? = null,
    val firstAired : Int? = null,
) {
    val isShow = show != null
    // from movie/show
    val title = if (isShow) show!!.title else movie!!.title
    val year = if (isShow) show!!.year else movie!!.year
    val ids = if (isShow) show!!.ids else movie!!.ids
    val status = if (isShow) show!!.status else movie!!.status // "in production", "canceled", "released"
    val overview = if (isShow) show!!.overview else movie!!.overview
}


fun TraktCreditDto.toDomain(): PersonCredit {
    return PersonCredit(
        isShow = isShow,
        //
        title = title,
        year = year,
        ids = ids,
        status = status,
        overview = overview,
        //
        character = character,
        // solo shows
        seriesRegular = seriesRegular,
        episodeCount = episodeCount,
        // solo crew
        job = job,
        jobs = jobs
    )
}

// test person credits:
// slug: david-corennswet | superman
// trakt: 852412
// tmdb: 1785590

