package com.example.muvitracker.data

import com.example.muvitracker.data.dto.movie.BoxofficeDtoM
import com.example.muvitracker.data.dto.movie.detail.DetailMovieTraktDto
import com.example.muvitracker.data.dto.show.detail.DetailShowTraktDto
import com.example.muvitracker.data.dto.episode.EpisodeTraktDto
import com.example.muvitracker.data.dto.movie.MovieBaseDto
import com.example.muvitracker.data.dto.SearchDto
import com.example.muvitracker.data.dto.movie.AnticipatedDtoM
import com.example.muvitracker.data.dto.movie.FavoritedDtoM
import com.example.muvitracker.data.dto.movie.WatchedDtoM
import com.example.muvitracker.data.dto.season.SeasonTraktDto
import com.example.muvitracker.data.dto.show.AnticipatedDtoS
import com.example.muvitracker.data.dto.show.FavoritedDtoS
import com.example.muvitracker.data.dto.show.ShowBaseDto
import com.example.muvitracker.data.dto.show.WatchedDtoS
import com.example.muvitracker.data.dto.person.CastResponseDto
import com.example.muvitracker.data.dto.person.PersonExtendedDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TraktApi {

    companion object {
        const val SEARCH_LIMIT = "24"
    }

    // SEARCH
    // type: movie, show, person
    @GET("search/{type_filter}?limit=$SEARCH_LIMIT")
    suspend fun getSearch(
        @Path("type_filter") typeFilter: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): List<SearchDto>


    // MOVIES ############################################## OK
    // todo filters - genre, year

    // ?page={page}&limit={limit} - impostazione standard
    @GET("movies/popular")
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


    // SHOWS ######################################################## OK
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
    ): List<WatchedDtoS>

    @GET("shows/favorited/weekly")
    suspend fun getFavoritedShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<FavoritedDtoS>

    @GET("shows/anticipated")
    suspend fun getAnticipatedShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<AnticipatedDtoS>


    // DETAIL MOVIE/SHOW
    @GET("movies/{movie_id}?extended=full")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int)
            : DetailMovieTraktDto


    // seasons(1,2,3,4...n), cast, related
    // 1 detailDto OK -
    // https://api.trakt.tv/shows/id
    @GET("shows/{show_id}?extended=full")
    suspend fun getShowDetail(@Path("show_id") showId: Int): DetailShowTraktDto

    // 2 all seasons OK
    // https://api.trakt.tv/shows/id/seasons/?extended=full
    @GET("shows/{show_id}/seasons/?extended=full")
    suspend fun getAllSeasons(@Path("show_id") showId: Int): List<SeasonTraktDto>


    // 3 related movie, show
    @GET("movies/{movie_id}/related")
    suspend fun getMovieRelatedMovies(
        @Path("movie_id") movieId: Int
    ): List<MovieBaseDto>

    @GET("shows/{show_id}/related")
    suspend fun getShowRelatedShows(
        @Path("show_id") showId: Int
    ): List<ShowBaseDto>


    // SEASON FRAGMENT
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


    // TODO CAST, PERSON DETAIL  ######################################################

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
    ): PersonExtendedDto


}


