package com.example.muvitracker.inkotlin.data.detail

import com.example.muvitracker.inkotlin.data.RetrofitUtils
import com.example.muvitracker.inkotlin.data.dto.DetailDto
import com.example.muvitracker.myappunti.kotlin.RetrofitCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


object OldDetaNetworkDS {

    private val traktApi = RetrofitUtils.traktApi


    fun callDetaServer(inputMovieId: Int, myCall: RetrofitCallback<DetailDto>) {

        val call: Call<DetailDto> = traktApi.getMovieDetails(movieId = inputMovieId) // movieId

        call.enqueue(object : Callback<DetailDto> {

            override fun onResponse(
                call: Call<DetailDto>,
                response: Response<DetailDto>
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
                call: Call<DetailDto>,
                t: Throwable
            ) {
                myCall.onError(t)

                println("ON_DETA_REPO_ERROR 2")
            }

        })


    }




}