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
import okhttp3.OkHttpClient
import okhttp3.Request
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


    // per app -> vai al link
    @Provides
    @Singleton
    fun provideHttpsClient (): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    fun provideRequestBuilder(): Request.Builder {
        return Request.Builder()
    }





}