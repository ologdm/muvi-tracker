package com.example.muvitracker.data.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.example.muvitracker.data.TmdbApi
import java.io.InputStream

class TmdbLoaderFactory(
    private val api: TmdbApi,
) : ModelLoaderFactory<ImageTmdbRequest, InputStream> {

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ImageTmdbRequest, InputStream> {
        return TmdbModelLoader(api)
    }

    override fun teardown() {}
}


class TmdbModelLoader(
    private val api: TmdbApi,
) : ModelLoader<ImageTmdbRequest, InputStream> {

    override fun buildLoadData(
        model: ImageTmdbRequest,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream> {
        return ModelLoader.LoadData(
            ObjectKey(model),
            TmdbFetcher(model, api)
        )

    }

    override fun handles(model: ImageTmdbRequest): Boolean = true
}