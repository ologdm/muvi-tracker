package com.example.muvitracker.data.imagetmdb.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.IGNORE
import com.bumptech.glide.module.AppGlideModule
import com.example.muvitracker.data.imagetmdb.TmdbApi
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.InputStream


@GlideModule
class MuviGlideModule : AppGlideModule() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GlideComponent {
        fun api(): TmdbApi
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val component = EntryPoints.get(context, GlideComponent::class.java)
        val api = component.api()
        registry.prepend(ImageTmdbRequest::class.java, InputStream::class.java, TmdbLoaderFactory(api))
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val level = IGNORE // ignora gli errori che non servono (e)
        builder.setLogLevel(Log.ERROR)
            .setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(level))
            .setSourceExecutor(GlideExecutor.newSourceExecutor(level))
    }
}