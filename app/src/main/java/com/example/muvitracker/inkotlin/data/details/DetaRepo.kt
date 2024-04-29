package com.example.muvitracker.inkotlin.data.details

import android.content.Context
import com.example.muvitracker.inkotlin.data.dto.DetaDto
import com.example.muvitracker.inkotlin.utils.EmptyStatesCallback
import com.example.muvitracker.myappunti.kotlin.RetrofitCallback
import java.io.IOException


/**
 * - repo gestisce logica flusso dati, non salva
 * - i film gli salvo solo la prima volta, no aggiornamento
 *
 *
 *  1° GET FUNZIONI:
 *
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
 *  2° SET FUNZIONI:
 *  -toggleFavoriteOnDB() OK
 *              > change liked on dto and create copy
 *              > updateDb
 *
 *  -updateWatchedOnDB() OK
 *              > updateDb
 *
 */


class DetaRepo
private constructor(
    private val context: Context
) {

    val detaLocalDS = DLocalDS.getInstance(context)


    // ########################################################
    // GET FUNZIONI

    fun getMovie(movieId: Int, callES: EmptyStatesCallback<DetaDto>) {
        var index = detaLocalDS.getItemIndex(movieId)

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
        DNetworkDS.callDetaServer(
            movieId,
            object : RetrofitCallback<DetaDto> {
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
    }


    fun getLocalItem(movieId: Int): DetaDto {
        return detaLocalDS.readItem(movieId)
    }


    // ########################################################
    // SET FUNZIONI

    // OK
    fun toggleFavoriteOnDB(inputDto: DetaDto) {
        // !!! se l'ho aperto, e gia su DB !!!
        val dtoModificato = inputDto.copy(liked = !inputDto.liked)
        detaLocalDS.updateItem(dtoModificato)
        println("XXX_REPO_TOGGLE_UPDATE_DB")
    }

    // OK
    fun updateWatchedOnDB(inputDto: DetaDto) {
        // invio dto modificato
        detaLocalDS.updateItem(inputDto)
        println("XXX_DETAREPO_WATCHEDUPDATEDB")
    }


    companion object {
        private var instance: DetaRepo? = null
        fun getInstance(context: Context): DetaRepo {
            if (instance == null) {
                instance = DetaRepo(context)
            }
            return instance!!
        }
    }

}





