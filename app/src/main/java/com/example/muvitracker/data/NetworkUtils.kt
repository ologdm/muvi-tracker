package com.example.muvitracker.data

import com.example.muvitracker.utils.IoResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 *  - API da retrofit OK
 *  - funzione enqueue generica OK
 */

object RetrofitUtils {

    private val retrofit = Retrofit.Builder()
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


    val traktApi = com.example.muvitracker.data.RetrofitUtils.retrofit.create(com.example.muvitracker.data.TraktApi::class.java)


    // ottieni call
//    fun getBoxoList(): Call<List<BoxoDto>> {
//        return traktApi.getBoxofficeMovies()
//    }
//
//
//    fun getPopuList(): Call<List<PopuDto>> {
//        return traktApi.getPopularMovies()
//    }

}


// EXTENSION FUNCTION

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


// TODO generica
//fun <T> Call<T>.startNetworkCallByQuery(
//    queryText: String,
//    onResponse: (IoResponse<T>) -> Unit
//) {
//    this.enqueue(object : Callback<T> {
//        override fun onResponse(call: Call<T>, response: Response<T>) {
//            if (response.isSuccessful) {
//                onResponse(IoResponse.Success(response.body()!!))
//            } else {
//                val exception = HttpException(response)
//                exception.printStackTrace()
//                onResponse(IoResponse.OtherError)
//            }
//        }
//
//        override fun onFailure(call: Call<T>, t: Throwable) {
//            t.printStackTrace()
//            onResponse(IoResponse.NetworkError)
//        }
//    })
//}






