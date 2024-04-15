package com.example.muvitracker.myappunti.kotlin

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/** RETROFIT UTILS
 * Interfacce retrofit
 *
 * funzioni: createMuviTrackerRetrofit() -> crea Istanza Retrofit
 */

// INTERFACCE

// liste
interface RetrofitCallbackList<T> {
    fun onSuccess(serverList: List<T>);
    fun onError(throwable: Throwable)
}


// singolo
interface RetrofitCallback<T> {
    fun onSuccess(serverItem: T);
    fun onError(throwable: Throwable)
}



// 1. con object
// - piu corretto, no spam in namespace global
object MyRetrofit {

    fun createMuviTrackerRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.trakt.tv/")
            .addConverterFactory(GsonConverterFactory.create())
            .callFactory(
                OkHttpClient.Builder()
                    .addInterceptor(Interceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader(
                                "trakt-api-key",
                                "d3dd937d16c8de9800f9ce30270ddc1d9939a2dafc0cd59f0a17b72a2a4208fd"
                            )
                            .build()
                        chain.proceed(newRequest)
                    })
                    .build()
            )
            .build()
        return retrofit
    }

}



// INCAPSULAMENTO CREAZIONE RETROFIT PER MUVI_TRACKER

/* 2. Funzione Globale
*     disponibile dapperutto, spam in namespace global

fun createMuviTrackerRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.trakt.tv/")
        .addConverterFactory(GsonConverterFactory.create())
        .callFactory(
            OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader(
                            "trakt-api-key",
                            "d3dd937d16c8de9800f9ce30270ddc1d9939a2dafc0cd59f0a17b72a2a4208fd"
                        )
                        .build()
                    chain.proceed(newRequest)
                })
                .build()
        )
        .build()
    return retrofit
}
 */




// 3. Extended function
// - e un metodo su un tipo gia esistente (istanza)
// - retrifit non esiste ancora
// - extension posso crearlo su builder
/*
fun Retrofit.Builder.createMuviTrackerRetrof(): Retrofit {
    val retrofit = this
        .baseUrl("https://api.trakt.tv/")
        .addConverterFactory(GsonConverterFactory.create())
        .callFactory(
            OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader(
                            "trakt-api-key",
                            "d3dd937d16c8de9800f9ce30270ddc1d9939a2dafc0cd59f0a17b72a2a4208fd"
                        )
                        .build()
                    chain.proceed(newRequest)
                })
                .build()
        )
        .build()
    return retrofit
}
 */