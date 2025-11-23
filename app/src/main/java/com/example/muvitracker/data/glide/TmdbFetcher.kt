package com.example.muvitracker.data.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.model.GlideUrl

import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.dto.movie.detail.MovieTmdbDto
import com.example.muvitracker.data.dto.show.detail.ShowTmdbDto
import kotlinx.coroutines.runBlocking
import java.io.InputStream

private const val TIMEOUT = 10_000 // 10 seconds
//private const val TMDB_IMAGE_URL_DOMAIN = "http://image.tmdb.org/t/p/original/"
// w = larghezza pixel
private const val TMDB_IMAGE_URL_DOMAIN_HORIZONTAL = "https://image.tmdb.org/t/p/w780/"
private const val TMDB_IMAGE_URL_DOMAIN_VERTICAL = "https://image.tmdb.org/t/p/w500/"


class TmdbFetcher(
    private val model: ImageTmdbRequest,
    private val tmdbApi: TmdbApi,
//    private val showStore: Store<Int,  DetailShow>,
) : DataFetcher<InputStream> {

    override fun loadData(
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>
    ) = runBlocking {
        when (model) {
            // when viene fatta per ogni tipo di richiesta
            is ImageTmdbRequest.MovieVertical -> fetchMovieImage(
                model.movieId,
                imagePathType = { it.posterPath },
                isVertical = true,
                priority,
                callback
            )

            is ImageTmdbRequest.MovieHorizontal -> fetchMovieImage(
                model.movieId,
                imagePathType = { it.backdropPath },
                isVertical = false,
                priority,
                callback
            )

            is ImageTmdbRequest.ShowVertical -> fetchShowImage(
                model.showId,
                imagePathType = { it.posterPath },
                isVertical = true,
                priority,
                callback
            )

            is ImageTmdbRequest.ShowHorizontal -> fetchShowImage(
                model.showId,
                imagePathType = { it.backdropPath },
                isVertical = false,
                priority,
                callback
            )

            is ImageTmdbRequest.Season -> fetchSeasonImage(model, priority, callback)
            is ImageTmdbRequest.Episode -> fetchEpisodeImage(model, priority, callback)
            is ImageTmdbRequest.Person -> fetchPersonImage(model, priority, callback)
        }
    }


    // TODO 1.1.3 OK
    private suspend fun fetchMovieImage(
        movieId: Int,
        imagePathType: (MovieTmdbDto) -> String?,
        isVertical: Boolean,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getMovieDto(movieId)
            val imagePath = imagePathType(response) ?: throw Exception("Image path not available")
            val baseUrl = if (isVertical) TMDB_IMAGE_URL_DOMAIN_VERTICAL else TMDB_IMAGE_URL_DOMAIN_HORIZONTAL
//            val url = "$TMDB_IMAGE_URL_DOMAIN${imagePath}"
            val url = "$baseUrl$imagePath"
            val fetcher = HttpUrlFetcher(GlideUrl(url), TIMEOUT)
            fetcher.loadData(priority, callback)
        } catch (ex: Exception) {
            callback.onLoadFailed(ex)
        }
    }


    // TODO 1.1.3 OK
    private suspend fun fetchShowImage(
        showId: Int,
        imagePathType: (ShowTmdbDto) -> String?,
        isVertical: Boolean,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getShowDto(showId)
            val imagePath = imagePathType(response) ?: throw Exception("Image path not available")
            val baseUrl = if (isVertical) TMDB_IMAGE_URL_DOMAIN_VERTICAL else TMDB_IMAGE_URL_DOMAIN_HORIZONTAL
            val url = "$baseUrl${imagePath}"
            val fetcher = HttpUrlFetcher(GlideUrl(url), TIMEOUT)
            fetcher.loadData(priority, callback)
        } catch (ex: Exception) {
            callback.onLoadFailed(ex)
        }
    }


    private suspend fun fetchSeasonImage(
        model: ImageTmdbRequest.Season,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getSeasonDto(model.showId, model.seasonNr)
            val url = "$TMDB_IMAGE_URL_DOMAIN_VERTICAL${response.posterPath}"
            val fetcher = HttpUrlFetcher(GlideUrl(url), TIMEOUT)
            fetcher.loadData(priority, callback)
        } catch (ex: Exception) {
            callback.onLoadFailed(ex)
        }
    }


    private suspend fun fetchEpisodeImage(
        model: ImageTmdbRequest.Episode,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getEpisodeDto(model.showId, model.seasonNr, model.episodeNr)
            val url = "$TMDB_IMAGE_URL_DOMAIN_HORIZONTAL${response.stillPath}"
            val fetcher = HttpUrlFetcher(GlideUrl(url), TIMEOUT)
            fetcher.loadData(priority, callback)
        } catch (ex: Exception) {
            callback.onLoadFailed(ex)
        }
    }

    private suspend fun fetchPersonImage(
        model: ImageTmdbRequest.Person,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getPersonDto(model.personId)
            val url = "$TMDB_IMAGE_URL_DOMAIN_VERTICAL${response.profilePath}"
            val fetcher = HttpUrlFetcher(GlideUrl(url), TIMEOUT)
            fetcher.loadData(priority, callback)
        } catch (ex: Exception) {
            callback.onLoadFailed(ex)
        }
    }


    override fun getDataClass(): Class<InputStream> = InputStream::class.java
    override fun getDataSource(): DataSource = DataSource.REMOTE
    override fun cleanup() {}
    override fun cancel() {}
}



