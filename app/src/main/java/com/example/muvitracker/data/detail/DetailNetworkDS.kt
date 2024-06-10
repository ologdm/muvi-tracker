package com.example.muvitracker.data.detail

import com.example.muvitracker.data.RetrofitUtils
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.utils.IoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


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
                    onResponse(IoResponse.Success(response.body()!!))
                } else {
                    val httpException = HttpException(response)
                    httpException.printStackTrace()
                    onResponse(IoResponse.OtherError)
                }
            }

            override fun onFailure(
                call: Call<DetailDto>,
                t: Throwable
            ) {
                t.printStackTrace()
                onResponse(IoResponse.NetworkError)
            }
        })
    }
}
