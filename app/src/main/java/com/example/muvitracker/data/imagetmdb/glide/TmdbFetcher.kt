package com.example.muvitracker.data.imagetmdb.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.example.muvitracker.data.imagetmdb.TmdbApi
import com.example.muvitracker.data.imagetmdb.dto.TmdbMovieShowDto
import kotlinx.coroutines.runBlocking
import java.io.InputStream

private const val TIMEOUT = 10_000 // 10 seconds
private const val TMDB_IMAGE_URL_DOMAIN = "http://image.tmdb.org/t/p/w500/"


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

            is ImageTmdbRequest.Season -> TODO()
            is ImageTmdbRequest.Episode -> TODO()
            is ImageTmdbRequest.Person -> TODO()
        }
    }


    private suspend fun fetchMovieImage(
        movieId: Int,
        imagePathType: (TmdbMovieShowDto) -> String?,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getMovieDtoImages(movieId)
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
        imagePathType: (TmdbMovieShowDto) -> String?,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = tmdbApi.getShowDtoImages(showId)
            val imagePath = imagePathType(response) ?: throw Exception("Image path not available")
            val url = "$TMDB_IMAGE_URL_DOMAIN${imagePath}"
            val fetcher = HttpUrlFetcher(GlideUrl(url), TIMEOUT)
            fetcher.loadData(priority, callback)
        } catch (ex: Exception) {
            callback.onLoadFailed(ex)
        }
    }

//    private suspend fun fetchSeasonImage(
//        model: ImageTmdbRequest.Season,
//        priority: Priority,
//        callback: DataFetcher.DataCallback<in InputStream>,
//    ) {
//        try {
//            val response = api.getShowDtoTest(model.showId,model.seasonNr)
//            val url = "$TMDB_IMAGE_URL_DOMAIN${response.posterPath}"
//            val fetcher = HttpUrlFetcher(GlideUrl(url), TIMEOUT)
//            fetcher.loadData(priority, callback)
//        } catch (ex: Exception) {
//            callback.onLoadFailed(ex)
//        }
//    }


    override fun getDataClass(): Class<InputStream> = InputStream::class.java
    override fun getDataSource(): DataSource = DataSource.REMOTE
    override fun cleanup() {}
    override fun cancel() {}
}



