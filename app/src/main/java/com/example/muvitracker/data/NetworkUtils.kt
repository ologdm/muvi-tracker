package com.example.muvitracker.data

import com.example.muvitracker.utils.IoResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun getApi(): TraktApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.trakt.tv/")
            .addConverterFactory(GsonConverterFactory.create())
            .callFactory(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader(
                                "trakt-api-key",
                                "d3dd937d16c8de9800f9ce30270ddc1d9939a2dafc0cd59f0a17b72a2a4208fd"
                            )
                            .build()
                        chain.proceed(newRequest)
                    }
                    .build()
            )
            .build()

        return retrofit.create(TraktApi::class.java)
    }
}


// ######################################################################

fun <T> Call<T>.startNetworkCall(onResponse: (IoResponse<T>) -> Unit) {

    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                onResponse(IoResponse.Success(response.body()!!))
            } else {
                val httpException = HttpException(response)
                httpException.printStackTrace()
                onResponse(IoResponse.OtherError)
            }
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            throwable.printStackTrace()
            onResponse(IoResponse.NetworkError)
        }

    })
}







