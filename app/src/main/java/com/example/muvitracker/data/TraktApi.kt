package com.example.muvitracker.data

import com.example.muvitracker.data.dto.BoxoDto
import com.example.muvitracker.data.dto.DetailMovieDto
import com.example.muvitracker.data.dto.DetailShowDto
import com.example.muvitracker.data.dto.EpisodeExtenDto
import com.example.muvitracker.data.dto.basedto.MovieBaseDto
import com.example.muvitracker.data.dto.SearchDto
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.data.dto.basedto.ShowBaseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TraktApi {

    @GET("search/movie")  // with mobile query
    suspend fun getSearch(@Query("query") searchString: String)
            : List<SearchDto>


    // MOVIES ##############################################

    // ?page={page}&limit={limit} - impostazione standard
    @GET("movies/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int, // standard
        @Query("limit") limit: Int // standard
    ): List<MovieBaseDto>


    @GET("movies/boxoffice") // no paging, only 10 results
    suspend fun getBoxoMovies(): List<BoxoDto>


    @GET("movies/{movie_id}?extended=full")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int)
            : DetailMovieDto


    // SHOWS ########################################################

    @GET("shows/popular")
    suspend fun getPopularShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<ShowBaseDto>

    // TODO https://api.trakt.tv/shows/trending

    // DETAIL
    // seasons(1,2,3,4...n), cast, related
    // 1 detailDto OK - // https://api.trakt.tv/shows/id
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
    suspend fun getEpisodeInfo (
        @Path("show_id") showId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): EpisodeExtenDto
}

