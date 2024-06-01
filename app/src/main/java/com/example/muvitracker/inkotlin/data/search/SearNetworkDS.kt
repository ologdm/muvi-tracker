package com.example.muvitracker.inkotlin.data.search

import com.example.muvitracker.inkotlin.data.RetrofitUtils
import com.example.muvitracker.inkotlin.data.dto.SearchDto
import com.example.muvitracker.inkotlin.utils.IoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

object SearNetworkDS {

    val traktApi = RetrofitUtils.traktApi


    fun getServer(
        queryText: String,
        onResponse: (IoResponse<List<SearchDto>>) -> Unit
    ) {
        val searchCall = traktApi.getSearch(queryText)

        searchCall.enqueue(object : Callback<List<SearchDto>> {
            override fun onResponse(
                call: Call<List<SearchDto>>, response: Response<List<SearchDto>>
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
                call: Call<List<SearchDto>>, t: Throwable
            ) {
                t.printStackTrace()
                onResponse(IoResponse.NetworkError)
            }
        })
    }

}




