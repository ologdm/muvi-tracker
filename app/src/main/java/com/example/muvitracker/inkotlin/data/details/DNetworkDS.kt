package com.example.muvitracker.inkotlin.data.details

import com.example.muvitracker.inkotlin.data.RetrofitUtils
import com.example.muvitracker.inkotlin.data.dto.DetaDto
import com.example.muvitracker.myappunti.kotlin.RetrofitCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


object DNetworkDS {

    private val traktApi = RetrofitUtils.traktApi


    fun callDetaServer(inputMovieId: Int, myCall: RetrofitCallback<DetaDto>) {

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