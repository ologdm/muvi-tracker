package com.example.muvitracker.inkotlin.mainactivity.popu.repo

import android.content.Context
import com.example.muvitracker.inkotlin.repo.dto.PopuDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallbackList
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import java.io.IOException

/*
// koltin
// object: - classe singleton
//  getIstance(conParametro) devo farlo nel modo classico

 */


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
 */

// cache - immagini e altri file temporanei, no istanze, liste
// storage - chache su disco, shared, db

/* appunti singleton:
   x ?: y(par:Par) {...}  - operatore elvis, controlla se x è null
   synchronized - evita conflitti in multithreading
   */


class PopuRepo
private constructor(
    private val context: Context // dichiarazione context
) {

    // ATTRIBUTI
    private val popuLocalDS = PopuLocalDS.getInstance(context)


    // METODI

    // 1
    fun getMovieList(callES: EmptyStatesCallbackList<PopuDto>) {

        callES.onSuccess(getCacheList()) // TODO 1 carica da locale
        println("XXX_POPU_REPO_SUCCESS LOCAL")

        callES.OnStart() // ES

        PopuNetworkDS.callPopuServer(object : RetrofitCallbackList<PopuDto> {

            override fun onSuccess(serverList: List<PopuDto>) {

                callES.onSuccess(serverList) // TODO 2 carica da server

                popuLocalDS.saveListInLocal(serverList) // TODO 2 save

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
            if (instance==null){
                instance=PopuRepo(context)
            }
            return instance!!
        }
    }


}









