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
            /**
             * RELEASE 1.1.3 - Handles glide cache invalidation when the system language changes.
             * The model key is a composite of ID + language, ensuring:
             * 1. Cache entries are invalidated if the language changes.
             *
             * Note: Correct image updates depend on the language correctly passed to Retrofit via its API.
             */
            ObjectKey(model),
            TmdbFetcher(model, api)
        )

    }

    override fun handles(model: ImageTmdbRequest): Boolean = true
}