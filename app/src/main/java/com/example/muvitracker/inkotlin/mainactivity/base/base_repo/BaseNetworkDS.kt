package com.example.muvitracker.inkotlin.mainactivity.popu.repo

import com.example.muvitracker.inkotlin.repo.TraktApi
import com.example.muvitracker.inkotlin.repo.dto.PopuDto
import com.example.muvitracker.myappunti.kotlin.MyRetrofit
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


object BaseNetworkDS {


    private val retrofit = MyRetrofit.createMuviTrackerRetrofit()
    /*
    val retrofit: Retrofit = createMuviTrackerRetrofit() ==> alternativa global function
    val retrofit = Retrofit.Builder().createMuviTrackerRetrof() ==> alternativa extended function
     */

    private val traktApi: TraktApi = retrofit.create(TraktApi::class.java)


    fun callPopuServer(callbackPresenter: RetrofitCallbackList<PopuDto>) {

        val call = traktApi.getPopularMovies()

        call.enqueue(object : Callback<List<PopuDto>> {

            override fun onResponse(
                call: Call<List<PopuDto>>,
                response: Response<List<PopuDto>>
            ) {

                if (response.isSuccessful) {
                    /*!! - not-null assertion operator, non sarà mai nullo
                    * response.body() nullable, quindi devo mettere !!
                    * not nullable e garantito da if sopra */
                    callbackPresenter.onSuccess(response.body()!!)

                    println("XXX_POPU_NETWORK_SUCCESS")
                } else {
                    println("Request failed with code: ${response.code()}")
                    callbackPresenter.onError(HttpException(response))

                    println("XXX_POPU_NETWORK_ERROR1")
                }
            }

            override fun onFailure(
                call: Call<List<PopuDto>>,
                t: Throwable
            ) {
                callbackPresenter.onError(t)

                println("XXX_POPU_NETWORK_ERROR2")
            }

        })


    }


}