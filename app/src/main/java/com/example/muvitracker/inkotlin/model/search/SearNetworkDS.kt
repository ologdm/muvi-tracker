package com.example.muvitracker.inkotlin.model.search

import com.example.muvitracker.inkotlin.model.TraktApi
import com.example.muvitracker.inkotlin.model.dto.search.SearDto
import com.example.muvitracker.myappunti.kotlin.MyRetrofit
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

object SearNetworkDS {


    val retrofit = MyRetrofit.createMuviTrackerRetrofit()


    val traktApi = retrofit.create(TraktApi::class.java)


    // FUNZIONI
    fun getServer(queryText: String, myCall: RetrofitCallbackList<SearDto>) {

        var searchCall = traktApi.getSearch(queryText)


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




