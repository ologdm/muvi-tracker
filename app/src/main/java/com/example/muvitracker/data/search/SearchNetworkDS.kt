package com.example.muvitracker.data.search

import com.example.muvitracker.data.RetrofitUtils
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.SearchDto
import com.example.muvitracker.utils.IoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


object SearchNetworkDS {

    private val traktApi: TraktApi = RetrofitUtils.traktApi


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
                    onResponse(IoResponse.Success(response.body()!!))
                    println()
                } else {
                    val exception = HttpException(response)
                    exception.printStackTrace()
                    onResponse(IoResponse.OtherError)
                    println()
                }
            }

            override fun onFailure(
                call: Call<List<SearchDto>>, t: Throwable
            ) {
                t.printStackTrace()
                onResponse(IoResponse.NetworkError)
                println()
            }
        })
    }

}




