package com.example.muvitracker.ui.main.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.imagetmdb.TmdbApi
import java.io.InputStream

class TmdbLoaderFactory(
    private val api: TmdbApi,
) : ModelLoaderFactory<ImageRequest, InputStream> {

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ImageRequest, InputStream> {
        return TmdbModelLoader(api)
    }

    override fun teardown() {}
}


class TmdbModelLoader(
    private val api: TmdbApi,
) : ModelLoader<ImageRequest, InputStream> {

    override fun buildLoadData(
        model: ImageRequest,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream> {
        return ModelLoader.LoadData(
            ObjectKey(model),
            TmdbFetcher(model, api)
        )

    }

    override fun handles(model: ImageRequest): Boolean = true
}