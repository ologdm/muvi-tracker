package com.example.muvitracker.data.detail

import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.utils.IoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

// OK IoResponse
object DetailNetworkDS {

    private val traktApi = com.example.muvitracker.data.RetrofitUtils.traktApi


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
