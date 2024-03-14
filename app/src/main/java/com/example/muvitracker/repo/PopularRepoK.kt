package com.example.muvitracker.repo

import com.example.muvitracker.repository.dto.PopularDtoK
import com.example.muvitracker.utils.kotlin.RetrofitListCallback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// object - classe singleton
// se devo creare classe con getIstance(conParametro) devo farlo nel modo classico


object PopularRepoK {


    // RETROFIT
    // Istanzio retrofit
    // TODO spostare tutto su funzione separata
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.trakt.tv/")
        .addConverterFactory(GsonConverterFactory.create())
        // Add Headers -> Trakt-Api-Key
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
        // ########################## fine call factor
        .build()


    // creo istanza api
    private val traktApi: TraktApiK = retrofit.create(TraktApiK::class.java)


    // creo chiamata
    fun callPopular(callbackPresenter: RetrofitListCallback<PopularDtoK>) {

        val call = traktApi.getPopularMovies()
        call.enqueue(object : Callback<List<PopularDtoK>> {
            override fun onResponse(
                call: Call<List<PopularDtoK>>,
                response: Response<List<PopularDtoK>>
            ) {
                if (response.isSuccessful) {
                    // !! - not-null assertion operator, non sarà mai nullo
                    // response.body() nullable, quindi devo mettere !!
                    // not nullable e garantito da if sopra
                    callbackPresenter.onSuccess(response.body()!!)
                } else {
                    println("Request failed with code: ${response.code()}")
                    callbackPresenter.onError(HttpException(response))
                }
            }

            override fun onFailure(
                call: Call<List<PopularDtoK>>, t: Throwable
            ) {
                callbackPresenter.onError(t)
            }

        })


    }


}

// devo definire le interfacce se ho 2 metodi

// anche se ho un metodo ?? o posso farlo direttamente nel lambda??










