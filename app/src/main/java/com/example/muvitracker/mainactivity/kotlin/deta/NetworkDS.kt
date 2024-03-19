package com.example.muvitracker.mainactivity.kotlin.deta

import com.example.muvitracker.repo.kotlin.TraktApiK
import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.MyRetrofit
import com.example.muvitracker.utils.kotlin.RetrofitCallbackK
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

// chiama il server e passa dati a repo


object NetworkDS {


    // crea retrofit
    private val retrofit = MyRetrofit.createMuviTrackerRetrofit()


    // crea api
    private val traktApi = retrofit.create(TraktApiK::class.java)


    fun callDetaServer(inputMovieId: Int, myCall: RetrofitCallbackK<DetaDto>) {

        val call: Call<DetaDto> = traktApi.getDetailsOfDto(traktMovieId = inputMovieId) // movieId

        call.enqueue(object : Callback<DetaDto> {

            override fun onResponse(
                call: Call<DetaDto>,
                response: Response<DetaDto>
            ) {
                if (response.isSuccessful) {
                    myCall.onSuccess(response.body()!!)

                    println("ON_DETA_REPO_SUCCESS")

                } else {
                    myCall.onError(HttpException(response))

                    println("ON_DETA_REPO_ERROR 1")
                }
            }

            override fun onFailure(
                call: Call<DetaDto>,
                t: Throwable
            ) {
                myCall.onError(t)

                println("ON_DETA_REPO_ERROR 2")
            }

        })


    }




}