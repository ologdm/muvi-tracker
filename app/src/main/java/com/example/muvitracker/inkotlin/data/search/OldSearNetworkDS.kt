package com.example.muvitracker.inkotlin.data.search

import com.example.muvitracker.inkotlin.data.RetrofitUtils
import com.example.muvitracker.inkotlin.data.dto.search.SearDto
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

object OldSearNetworkDS {

    val traktApi = RetrofitUtils.traktApi


    fun getServer(queryText: String, myCall: RetrofitCallbackList<SearDto>) {
        val searchCall = traktApi.getSearch(queryText)

        // implemento chiamata retrofit
        searchCall.enqueue(object : Callback<List<SearDto>> {

            override fun onResponse(
                call: Call<List<SearDto>>, response: Response<List<SearDto>>
            ) {
                if (response.isSuccessful) {
                    myCall.onSuccess(response.body()!!)
                    println("ON_SEARCH_NETWORK_SUCCESS")

                } else {
                    myCall.onError(HttpException(response))
                    println("ON_SEARCH_NETWORK_ERROR 1")
                }

            }

            override fun onFailure(
                call: Call<List<SearDto>>, t: Throwable
            ) {
                myCall.onError(t)
                println("ON_SEARCH_NETWORK_ERROR 2")
            }
        })

    }


}




