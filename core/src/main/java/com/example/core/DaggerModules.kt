package com.example.core

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

// SU CORE:
//  - REPO NO
//  - DATABASE NO

//  - SHARED SI
//  - GSON SI
//  - JSON
// - RETROFIT PROVIDER TODO SI

@Module
@InstallIn(SingletonComponent::class)
class DaggerModules {


    // shared preferences
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }


    // TODO OK
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys =
                true // ignora campi non presenti nel tuo data class
            isLenient =
                true // rende il parser più permissivo in lettura di JSON non perfetto
            encodeDefaults =
                true // (per POST, invio dati) include sempre i valori di default nella serializzazione in JSON
            coerceInputValues =
                true // GET - se trova un null in un campo non nullable, usa il valore di default invece di crashare.
        }
    }





    // TODO: 16 mar 2026 spostare retrofit su core
//    @OptIn(ExperimentalSerializationApi::class) // opt-in corretto per asConverterFactory
//    @Provides
//    @Singleton
//    fun provideTraktApi(): TraktApi {
//        val json = provideJson()
//        val contentType = "application/json".toMediaType()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.trakt.tv/")
//            .addConverterFactory(json.asConverterFactory(contentType))
//            .client( // al posto di callFactory()
//                OkHttpClient.Builder()
//                    .addInterceptor { chain ->
//                        val newRequest = chain.request().newBuilder()
//                            .addHeader("trakt-api-key", BuildConfig.TRAKT_API_KEY)
//                            .build()
//                        chain.proceed(newRequest)
//                    }.build()
//            )
//            .build()
//        return retrofit.create(TraktApi::class.java)
//    }


//    @Provides
//    @Singleton
//    fun provideTmdbApi(): TmdbApi {
//        val json = provideJson()
//        val contentType = "application/json".toMediaType()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.themoviedb.org/3/")
//            .addConverterFactory(json.asConverterFactory(contentType))
//            .build()
//        return retrofit.create(TmdbApi::class.java)
//    }


//    @Provides
//    @Singleton
//    fun provideOmdbApi(): OmdbApi {
//        val json = provideJson()
//        val contentType = "application/json".toMediaType()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://www.omdbapi.com/")
//            .addConverterFactory(json.asConverterFactory(contentType))
//            .build()
//        return retrofit.create(OmdbApi::class.java)
//    }



}