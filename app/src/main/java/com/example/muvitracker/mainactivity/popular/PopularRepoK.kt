package com.example.muvitracker.mainactivity.popular

import com.example.muvitracker.repo.TraktApiK
import com.example.muvitracker.repository.dto.PopularDtoK
import com.example.muvitracker.utils.kotlin.MyRetrofit
import com.example.muvitracker.utils.kotlin.RetrofitListCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

// koltin
// object: - classe singleton
//  getIstance(conParametro) devo farlo nel modo classico

// TODO: (esercizio eugi) usare 2 callback-lambda stile kotlin al posto di RetrofitListCallback<T>


object PopularRepoK {


    // Retrofit
    // 1.
     val retrofit = MyRetrofit.createMuviTrackerRetrofit()
    // val retrofit: Retrofit = createMuviTrackerRetrofit() ==> alternativa global function
    // val retrofit = Retrofit.Builder().createMuviTrackerRetrof() ==> alternativa extended function

    // 2.
    private val traktApi: TraktApiK = retrofit.create(TraktApiK::class.java)


    // 3.
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









