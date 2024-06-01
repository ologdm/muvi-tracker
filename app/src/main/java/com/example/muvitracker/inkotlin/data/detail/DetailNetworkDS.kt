package com.example.muvitracker.inkotlin.data.detail

import com.example.muvitracker.inkotlin.data.RetrofitUtils
import com.example.muvitracker.inkotlin.data.dto.DetailDto
import com.example.muvitracker.inkotlin.utils.IoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

// OK IoResponse
object DetailNetworkDS {

    private val traktApi = RetrofitUtils.traktApi


    fun callDetailServer(
        movieId: Int,
        onResponse: (IoResponse<DetailDto>) -> Unit
    ) {
        val call: Call<DetailDto> = traktApi.getMovieDetails(movieId = movieId)

        call.enqueue(object : Callback<DetailDto> {
            override fun onResponse(
                call: Call<DetailDto>,
                response: Response<DetailDto>
            ) {
                if (response.isSuccessful) {
//                    myCall.onSuccess(response.body()!!)
                    onResponse(IoResponse.Success(response.body()!!))  // 1
                } else {
//                    myCall.onError(HttpException(response))
                    val httpException = HttpException(response)
                    httpException.printStackTrace()
                    onResponse(IoResponse.OtherError) // 2
                }
            }

            override fun onFailure(
                call: Call<DetailDto>,
                t: Throwable
            ) {
//                myCall.onError(t)
                t.printStackTrace()
                onResponse(IoResponse.NetworkError) // 3
            }
        })
    }
}

//        myCall: RetrofitCallback<DetailDto>