package com.example.muvitracker.inkotlin.model.popu

import android.content.Context
import com.example.muvitracker.inkotlin.mainactivity.base.MovieModel
import com.example.muvitracker.inkotlin.mainactivity.base.toDomain
import com.example.muvitracker.inkotlin.model.dto.base.PopuDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallbackList
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import java.io.IOException

/**
 *
 * invio dati da locale poi da server
 *
 * FUNZIONI:
 *  1) fun getMovieList() OK
 *          > callback ES
 *              > success: locale passa lista
 *              > success: server passa lista e salva su db
 *
 *
 *  2) fun getCacheList() OK
 *                  > return lista in db
 *
 *
 *  3) Singleton OK
 *     fun getInstance(context)
 *
 *  step 8 - conversione da PopuDto a MovieModel OK
 *           modificata funzione getMovieList()
 *
 */


class PopuRepo
private constructor(
    private val context: Context
) {

    private val popuLocalDS = PopuLocalDS.getInstance(context)


    // METODI

    // 1
    fun getMovieList(callES: EmptyStatesCallbackList<MovieModel>) {

        // TODO !!! funzione conversione lista Dto a lista MovieModel
        callES.onSuccess(getCacheList().toDomain()) // 1 carica da locale


        callES.OnStart() // ES
        println("XXX_POPU_REPO_SUCCESS LOCAL")
        PopuNetworkDS.callPopuServer(object : RetrofitCallbackList<PopuDto> {

            override fun onSuccess(serverList: List<PopuDto>) {

                // TODO conversione a Domain
                callES.onSuccess(serverList.toDomain()) // 2 carica da server

                popuLocalDS.saveListInLocal(serverList) // 2 save

                println("XXX_POPU_REPO_SUCCESS_NETWORK")
            }

            override fun onError(throwable: Throwable) {
                if (throwable is IOException) {
                    throwable.printStackTrace()
                    callES.onErrorIO() // ES

                } else {
                    throwable.printStackTrace()
                    callES.onErrorOther() // ES
                }
            }
        })
    }


    // 2. solo da db
    private fun getCacheList(): List<PopuDto> {
        println("XXX_POPU_REPO_GET CACHE LIST")
        return popuLocalDS.loadFromLocal()
    }


    // Singleton OK
    companion object {
        private var instance: PopuRepo? = null

        fun getInstance(context: Context): PopuRepo {
            if (instance == null) {
                instance = PopuRepo(context)
            }
            return instance!!
        }
    }


}









