package com.example.muvitracker.mainactivity.kotlin.details

import com.example.muvitracker.repo.kotlin.TraktApiK
import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.MyRetrofit
import com.example.muvitracker.utils.kotlin.RetrofitCallbackK
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


object DetaRepo {


    // crea retrofit
    val retrofit = MyRetrofit.createMuviTrackerRetrofit()


    // crea api
    val traktApi = retrofit.create(TraktApiK::class.java)


    fun callDetalServer(inputMovieId: Int, myCall: RetrofitCallbackK<DetaDto>) {

        val call: Call<DetaDto> = traktApi.getDetailsOfDto(traktMovieId = inputMovieId) // movieId

        call.enqueue(object : Callback<DetaDto> {

            override fun onResponse(
                call: Call<DetaDto>,
                response: Response<DetaDto>
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
                call: Call<DetaDto>,
                t: Throwable
            ) {
                myCall.onError(t)

                println("ON_DETA_REPO_ERROR 2")
            }

        })


    }


}

// TODO: DETA REPO
// 1° step
// scarica i dati al click oggetto


// 2° step
// aggiungi locale e repo


// 3° step
// aggiungi logica (vedere sotto)


// repo -> decide da dove prendere info e inviarla al fragment
//   - locale shared prefs poi scarica e aggiorna
//   - scarica da server direttamente


// locale - implementazione shared prefs

// server - chiamata a retrofit

