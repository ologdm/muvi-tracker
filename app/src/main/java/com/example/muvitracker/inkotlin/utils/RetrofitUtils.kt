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



interface RetrofitCallbackList<T> {
    fun onSuccess(serverList: List<T>);
    fun onError(throwable: Throwable)
}



interface RetrofitCallback<T> {
    fun onSuccess(serverItem: T);
    fun onError(throwable: Throwable)
}


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
