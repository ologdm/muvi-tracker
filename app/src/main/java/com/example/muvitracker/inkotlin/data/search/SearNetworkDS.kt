package com.example.muvitracker.inkotlin.data.search

import com.example.muvitracker.inkotlin.data.RetrofitUtils
import com.example.muvitracker.inkotlin.data.dto.search.SearDto
import com.example.muvitracker.inkotlin.utils.IoResponse
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

object SearNetworkDS {

    val traktApi = RetrofitUtils.traktApi


    fun getServer(
        queryText: String,
        onResponse: (IoResponse<List<SearDto>>) -> Unit
    ) {
        val searchCall = traktApi.getSearch(queryText)

        searchCall.enqueue(object : Callback<List<SearDto>> {
            override fun onResponse(
                call: Call<List<SearDto>>, response: Response<List<SearDto>>
            ) {
                if (response.isSuccessful) {
                    onResponse(IoResponse.Success(response.body()!!)) // OK
                } else {
                    val exception = HttpException(response)
                    exception.printStackTrace()
                    onResponse(IoResponse.OtherError)
                }
            }

            override fun onFailure(
                call: Call<List<SearDto>>, t: Throwable
            ) {
                t.printStackTrace()
                onResponse(IoResponse.NetworkError)
            }
        })
    }

}




