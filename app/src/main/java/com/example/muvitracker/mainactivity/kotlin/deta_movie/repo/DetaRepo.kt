package com.example.muvitracker.mainactivity.kotlin.deta_movie.repo

import android.annotation.SuppressLint
import android.content.Context
import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.EmptyStatesCallback
import com.example.muvitracker.utils.kotlin.RetrofitCallback
import java.io.IOException


/**
 * repo gestisce logica flusso dati, non salva
 *
 *
 *  1° GET FUNZIONI:
 *  -getMovie() OK
 *      > sceglie tra DB e Server
 *      - getNetworkItemAndAddToLocal()  > assincrona, scarica e addDB+callback to presenter (success);
 *                                         gestisce empty states
 *
 *      - getLocalItem ()                > assincorna, con callback EmptyStatesCallback
 *
 *
 *  -getLocalItem ()  OK                 > sincrona - direttamente da DB
 *
 *
 *
 *
 *
 *  2° SET FUNZIONI:
 *  -toggleFavoriteOnDB() OK
 *              > change liked on dto and create copy
 *              > updateDb
 *
 *  -updateWatchedOnDB() OK
 *              > updateDb
 *
 *
 */


class DetaRepo(
    private val context: Context
) {


    companion object {

        // SINGLETON
        @Volatile
        @SuppressLint("StaticFieldLeak")
        private var instance: DetaRepo? = null

        fun getInstance(context: Context): DetaRepo {
            instance ?: synchronized(this) {
                instance ?: DetaRepo(context.applicationContext)
                    .also {
                        instance = it
                    }
            }
            return instance!!
        }

    }


    // TODO: perche me lo accetta xDetaLocalDS ???
    val detaLocalDS = DLocalDS.getInstance(context)


    // ########################################################
    // GET FUNZIONI


    fun getMovie(movieId: Int, callES: EmptyStatesCallback<DetaDto>) {
        // 1 TODO: carica dati da shared in RAM (solo una volta)

        var index = detaLocalDS.getItemIndex(movieId)

        // branch dell if assincrono non puo avere - return classico, return è  sempre sincrono
        if (index != -1) { // se trova index -> getDB

            getLocalItem(movieId, callES)

            println("XXX_REPO_GET_DB")

        } else {
            getNetworkItemAndAddToLocal(movieId, callES)

            println("XXX_REPO_GET_SERVER")
        }
    }


    // METODI PRIVATI
    // OK
    private fun getNetworkItemAndAddToLocal(movieId: Int, callES: EmptyStatesCallback<DetaDto>) {
        callES.OnStart() // empty states

        DNetworkDS.callDetaServer(movieId, object : RetrofitCallback<DetaDto> {

            override fun onSuccess(serverItem: DetaDto) {
                detaLocalDS.createItem(serverItem) // add DB

                callES.onSuccess(serverItem) //passo tu presenter

                println("XXX_REPO_SERVER_SUCCESS")
            }

            override fun onError(throwable: Throwable) {
                if (throwable is IOException) {
                    callES.onErrorIO()
                    println("XXX_REPO_SERVER_ERROR1")
                } else {
                    callES.onErrorOther()
                    println("XXX_REPO_SERVER_ERROR2")
                }
            }
        })
    }

    // OK
    private fun getLocalItem(movieId: Int, callES: EmptyStatesCallback<DetaDto>) {
        val databaseDto = detaLocalDS.readItem(movieId)
        callES.onSuccess(databaseDto)
        // println ok success

    }


    fun getLocalItem(movieId: Int): DetaDto {
        return detaLocalDS.readItem(movieId)
        // println su presenter
    }


    // ########################################################
    // SET FUNZIONI

    // OK
    fun toggleFavoriteOnDB(inputDto: DetaDto) {
        // !!! se l'ho aperto, e gia su DB !!!

        val dtoModificato = inputDto.copy(liked = !inputDto.liked)
        detaLocalDS.updateItem(dtoModificato)

        //inputDto =
        //localDS.updateItem(inputDto)

        println("XXX_REPO_TOGGLE_UPDATE_DB")
    }


    // OK
    fun updateWatchedOnDB(inputDto: DetaDto) {

        // invio dto modificato
        detaLocalDS.updateItem(inputDto)

        println("XXX_DETAREPO_WATCHEDUPDATEDB")

    }


}





