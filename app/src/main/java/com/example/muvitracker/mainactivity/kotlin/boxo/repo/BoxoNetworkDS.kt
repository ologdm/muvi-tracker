package com.example.muvitracker.mainactivity.kotlin.boxo.repo

import com.example.muvitracker.repo.kotlin.TraktApi
import com.example.muvitracker.repo.kotlin.dto.BoxoDto
import com.example.muvitracker.utils.kotlin.MyRetrofit
import com.example.muvitracker.utils.kotlin.RetrofitCallbackList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

object BoxoNetworkDS {


    private val retrofit = MyRetrofit.createMuviTrackerRetrofit() // istanza retrofit


    private val traktApi = retrofit.create(TraktApi::class.java) // crea istanza da classe java


    fun callBoxoServer(callbackPresenter: RetrofitCallbackList<BoxoDto>) {

        val call: Call<List<BoxoDto>> = traktApi.getBoxofficeMovies()


        call.enqueue(object : Callback<List<BoxoDto>> {

            override fun onResponse(
                call: Call<List<BoxoDto>>,
                response: Response<List<BoxoDto>>
            ) {
                if (response.isSuccessful) {
                    // !! - not-null assertion operator, non sarà mai nullo
                    callbackPresenter.onSuccess(response.body()!!)

                    println("XXX_BOXO_NET_SUCCESS")

                } else {
                    println("Request failed with code: ${response.code()}")
                    callbackPresenter.onError(HttpException(response))

                    println("XXX_BOXO__NET_REPO_ERROR1")
                }
            }

            override fun onFailure(
                call: Call<List<BoxoDto>>,
                t: Throwable
            ) {
                callbackPresenter.onError(t)

                println("XXX_BOXO__NET_REPO_ERROR2")
            }
        })

    }
}