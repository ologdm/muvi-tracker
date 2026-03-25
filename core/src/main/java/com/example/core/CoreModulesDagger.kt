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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModulesDagger {


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


    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys =
                true // ignores fields not present in your data class
            isLenient =
                true // makes the parser more permissive when reading imperfect JSON
            encodeDefaults =
                true // (for POST, sending data) always includes default values in JSON serialization
            coerceInputValues =
                true // GET - if it finds a null in a non-nullable field, it uses the default value instead of crashing
        }
    }


    // usages - retrofit, open links
    @Provides
    @Singleton
    fun provideHttpsClient (): OkHttpClient {
        return OkHttpClient()
        // NOTE:
        //  Client globale “pulito”
        //   - Impostazioni di default di OkHttp:
        //       • connectTimeout = 10s
        //       • readTimeout = 10s
        //       • writeTimeout = 10s
        //   - Non ha cache, logging o interceptor
        //   - Per API che richiedono header o comportamenti speciali (es. Trakt),
        //     usare client.newBuilder() per aggiungere solo quello che serve
        //   - In futuro, basta modificare qui timeout, cache o logging per tutti i Retrofit
    }


}