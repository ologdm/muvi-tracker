package com.example.muvitracker.data.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.dto.tmdb.DetailMovieDtoTmdb
import com.example.muvitracker.data.dto.tmdb.ShowDtoTmdb
import kotlinx.coroutines.runBlocking
import java.io.InputStream

private const val TIMEOUT = 10_000 // 10 seconds
private const val TMDB_IMAGE_URL_DOMAIN = "http://image.tmdb.org/t/p/original/"


class TmdbFetcher(
    private val model: ImageTmdbRequest,
    private val tmdbApi: TmdbApi,
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
                priority,
                callback
            )

            is ImageTmdbRequest.MovieHorizontal -> fetchMovieImage(
                model.movieId,
                imagePathType = { it.backdropPath },
                priority,
                callback
            )

            is ImageTmdbRequest.ShowVertical -> fetchShowImage(
                model.showId,
                imagePathType = { it.posterPath },
                priority,
                callback
            )

            is ImageTmdbRequest.ShowHorizontal -> fetchShowImage(
                model.showId,
                imagePathType = { it.backdropPath },
                priority,
                callback
            )

            is ImageTmdbRequest.Season -> fetchSeasonImage(model, priority, callback)
            is ImageTmdbRequest.Episode -> fetchEpisodeImage(model, priority, callback)
            is ImageTmdbRequest.Person -> fetchPersonImage(model, priority, callback)
        }
    }


    private suspend fun fetchMovieImage(
        movieId: Int,
        imagePathType: (DetailMovieDtoTmdb) -> String?,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getMovieDto(movieId)
            val imagePath = imagePathType(response) ?: throw Exception("Image path not available")
            val url = "$TMDB_IMAGE_URL_DOMAIN${imagePath}"
            val fetcher = HttpUrlFetcher(GlideUrl(url), TIMEOUT)
            fetcher.loadData(priority, callback)
        } catch (ex: Exception) {
            callback.onLoadFailed(ex)
        }
    }


    private suspend fun fetchShowImage(
        showId: Int,
        imagePathType: (ShowDtoTmdb) -> String?,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getShowDto(showId)
            val imagePath = imagePathType(response) ?: throw Exception("Image path not available")
            val url = "$TMDB_IMAGE_URL_DOMAIN${imagePath}"
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
            val url = "$TMDB_IMAGE_URL_DOMAIN${response.posterPath}"
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
            val url = "$TMDB_IMAGE_URL_DOMAIN${response.stillPath}"
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
            val url = "$TMDB_IMAGE_URL_DOMAIN${response.profilePath}"
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



