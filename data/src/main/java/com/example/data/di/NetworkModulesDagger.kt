package com.example.data.di

import com.example.core.BuildConfig
import com.example.data.api.OmdbApi
import com.example.data.api.TmdbApi
import com.example.data.api.TraktApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModulesDagger {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TraktRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TmdbRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OmdbRetrofit


    @Provides
    @Singleton
    fun provideConverterFactory(json: Json): Converter.Factory {
        val contentType = "application/json".toMediaType()
        return json.asConverterFactory(contentType)
    }


    // RETROFIT -----------------------------------------------------------------------------------

    @Provides
    @Singleton
    @TraktRetrofit
    fun provideTraktRetrofit(
        converterFactory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.trakt.tv/")
            .addConverterFactory(converterFactory)
            .client( // al posto di callFactory()
//                OkHttpClient.Builder()
                client.newBuilder() // eredita impostazione da altri builder - CHECK SERVRE
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("trakt-api-key", BuildConfig.TRAKT_API_KEY)
                            .build()
                        chain.proceed(newRequest)
                    }
                    // NOTE: per debug - intercettare json ricevuto
//                    .addInterceptor(HttpLoggingInterceptor().apply {
//                        level = HttpLoggingInterceptor.Level.BODY
//                    })
                    .build()
            )
            .build()
    }


    @Provides
    @Singleton
    @TmdbRetrofit
    fun provideTmdbRetrofit(
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(converterFactory)
            .build()
    }


    @Provides
    @Singleton
    @OmdbRetrofit
    fun provideOmdbRetrofit(
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(converterFactory)
            .build()
    }


    // API SERVICES -----------------------------------------------------------------------------------

    @Provides
    @Singleton
    fun provideTraktApi(
        @TraktRetrofit retrofit: Retrofit
    ): TraktApi {
        return retrofit.create(TraktApi::class.java)
    }


    @Provides
    @Singleton
    fun provideTmdbApi(
        @TmdbRetrofit retrofit: Retrofit
    ): TmdbApi {
        return retrofit.create(TmdbApi::class.java)
    }


    @Provides
    @Singleton
    fun provideOmdbApi(
        @OmdbRetrofit retrofit: Retrofit
    ): OmdbApi {
        return retrofit.create(OmdbApi::class.java)
    }


    // TODO: NEW FEATURES
    /**  ALL IMAGES FROM TMDB - attualmente non usato
     * -> data.unused.tmdb_all_images
     */
//    @Provides
//    @Singleton
//    fun provideTmdbAllImagesApi(): TmdbAllImagesApi {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.themoviedb.org/3/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        return retrofit.create(TmdbAllImagesApi::class.java)
//    }


}