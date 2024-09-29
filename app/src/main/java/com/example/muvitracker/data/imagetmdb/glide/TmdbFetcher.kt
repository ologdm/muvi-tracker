package com.example.muvitracker.data.imagetmdb.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.example.muvitracker.data.imagetmdb.TmdbApi
import kotlinx.coroutines.runBlocking
import java.io.InputStream

private const val TIMEOUT = 10_000 // 10 seconds

class TmdbFetcher(
    private val model: ImageRequest,
    private val api: TmdbApi,
) : DataFetcher<InputStream> {

    override fun loadData(
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>
    ) = runBlocking {
        when (model) {
            is ImageRequest.Episode -> fetchEpisodeImage(model, priority, callback)
        }
    }

    private suspend fun fetchEpisodeImage(
        model: ImageRequest.Episode,
        priority: Priority,
        callback: DataFetcher.DataCallback<in InputStream>,
    ) {
        try {
            val response = api.getEpisodeImages(model.seasonId, model.season, model.episode)
            val url = response.stills.first().filePath
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