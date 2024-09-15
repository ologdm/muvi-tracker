package com.example.muvitracker.data

import com.example.muvitracker.data.dto.movies.BoxofficeDtoM
import com.example.muvitracker.data.dto.movies.DetailMovieDto
import com.example.muvitracker.data.dto.show.DetailShowDto
import com.example.muvitracker.data.dto.episode.EpisodeExtenDto
import com.example.muvitracker.data.dto.movies.MovieBaseDto
import com.example.muvitracker.data.dto.SearchDto
import com.example.muvitracker.data.dto.movies.AnticipatedDtoM
import com.example.muvitracker.data.dto.movies.FavoritedDtoM
import com.example.muvitracker.data.dto.movies.WatchedDtoM
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.show.AnticipatedDtoS
import com.example.muvitracker.data.dto.show.FavoritedDtoS
import com.example.muvitracker.data.dto.show.ShowBaseDto
import com.example.muvitracker.data.dto.show.WatchedDtoS
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TraktApi {

    // SEARCH
    // type: movie, show, person
    @GET("search/{filter_type}")
    suspend fun getSearch(
        @Path("filter_type") filterType: String,
        @Query("query") searchString: String
    ): List<SearchDto>


    // MOVIES ############################################## OK
    // todo filters - gennre, year

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

    @GET("movies/boxoffice") // no paging, only 10 results
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
            : DetailMovieDto


    // seasons(1,2,3,4...n), cast, related
    // 1 detailDto OK -
    // https://api.trakt.tv/shows/id
    @GET("shows/{show_id}?extended=full")
    suspend fun getShowDetail(@Path("show_id") showId: Int): DetailShowDto

    // 2 all seasons OK
    // https://api.trakt.tv/shows/id/seasons/?extended=full
    @GET("shows/{show_id}/seasons/?extended=full")
    suspend fun getAllSeasons(@Path("show_id") showId: Int): List<SeasonExtenDto>


    // 3 cast - all cast dto TODO
//    suspend fun getAllCrew(): CrewDto


    // SEASON FRAGMENT
    // https://api.trakt.tv/shows/game-of-thrones/seasons/number?extended=full
    // season info
    @GET("shows/{show_id}/seasons/{season_number}/info?extended=full")
    suspend fun getSeasonInfo(
        @Path("show_id") showId: Int,
        @Path("season_number") seasonNumber: Int
    ): SeasonExtenDto

    // all episodes
    @GET("shows/{show_id}/seasons/{season_number}?extended=full")
    suspend fun getSeasonWithEpisodes(
        @Path("show_id") showId: Int,
        @Path("season_number") seasonNumber: Int
    ): List<EpisodeExtenDto>


    // EPISODE FRAGMENT (bottom sheet)
    @GET("shows/{show_id}/seasons/{season_number}/episodes/{episode_number}?extended=full")
    suspend fun getEpisodeInfo(
        @Path("show_id") showId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): EpisodeExtenDto
}


