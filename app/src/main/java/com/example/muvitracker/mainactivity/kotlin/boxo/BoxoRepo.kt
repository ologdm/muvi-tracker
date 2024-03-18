package com.example.muvitracker.mainactivity.kotlin.boxo

import com.example.muvitracker.repo.kotlin.TraktApiK
import com.example.muvitracker.repo.kotlin.dto.BoxoDto
import com.example.muvitracker.utils.kotlin.MyRetrofit
import com.example.muvitracker.utils.kotlin.RetrofitListCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

/*
// crea retrofit
// crea api da retrofit
// definisco call da api
// chiama enqueue
//                  call.enqueue => object : DichiaroTipo {}
//                  => poi dopo mi fa implementare i metodi dell'intefaccia

 */


object BoxoRepo {


    private val retrofit = MyRetrofit.createMuviTrackerRetrofit() // istanza retrofit


    private val traktApi = retrofit.create(TraktApiK::class.java) // crea istanza da classe java


    fun callBoxoServer(callbackPresenter: RetrofitListCallback<BoxoDto>) {

        // definisco call da api
        val call: Call<List<BoxoDto>> = traktApi.getBoxofficeMovies()

        // chiamata
        call.enqueue(object : Callback<List<BoxoDto>> {

            override fun onResponse(
                call: Call<List<BoxoDto>>,
                response: Response<List<BoxoDto>>
            ) {
                if (response.isSuccessful) {
                    // !! - not-null assertion operator, non sarà mai nullo
                    callbackPresenter.onSuccess(response.body()!!)

                    println("XXX_BOXO_REPO_SUCCESS")
                } else {
                    println("Request failed with code: ${response.code()}")
                    callbackPresenter.onError(HttpException(response))

                    println("XXX_BOXO_REPO_ERROR1")
                }
            }

            override fun onFailure(
                call: Call<List<BoxoDto>>,
                t: Throwable
            ) {
                callbackPresenter.onError(t)

                println("XXX_BOXO_REPO_ERROR2")
            }
        })

    }


}