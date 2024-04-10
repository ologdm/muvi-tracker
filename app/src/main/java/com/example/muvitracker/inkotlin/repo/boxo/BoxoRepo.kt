package com.example.muvitracker.inkotlin.repo.boxo

import android.content.Context
import com.example.muvitracker.inkotlin.repo.dto.base.BoxoDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallbackList
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import java.io.IOException


/**
 *
 *
 * FUNZIONI
 * 1) getMovieList -> get local and/or server
 * 2) getCacheList()
 *
 *
 */


class BoxoRepo
private constructor(
    private val context: Context
) {

    // ATTRIBUTI
    private val boxoLocalDS = BoxoLocalDS.getInstance(context)


    // SINGLETON OK
    companion object {
        private var instance: BoxoRepo? = null

        fun getInstance(context: Context): BoxoRepo {
            if (instance == null) {
                instance = BoxoRepo(context)
            }
            return instance!!
        }
    }


    // METODI

    // 1 get local o server
    fun getMovieList(callES: EmptyStatesCallbackList<BoxoDto>) {
        callES.OnStart() // ES
        callES.onSuccess(getCacheList()) // TODO 1 carica da locale
        println("XXX_BOXO_REPO_SUCCESS LOCAL")

        BoxoNetworkDS.callBoxoServer(object : RetrofitCallbackList<BoxoDto> {

            override fun onSuccess(serverList: List<BoxoDto>) {
                callES.onSuccess(serverList) // ES TODO 2 carica da server
                boxoLocalDS.saveListInLocal(serverList) // TODO 2 save
                println("XXX_BOXO_REPO_SUCCESS_NETWORK")
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


    // 2 get local
    private fun getCacheList(): List<BoxoDto> {
        println("XXX_BOXO_REPO_GET CACHE LIST")

        return boxoLocalDS.loadFromLocal()
    }


    /* 3
    private fun getServerList (){
        xBoxoNetworkDS.callBoxoServer(object :RetrofitCallbackList<BoxoDto>{
            override fun onSuccess(serverList: List<BoxoDto>) {

            }

            override fun onError(throwable: Throwable) {

            }

        })
    }

     */


}