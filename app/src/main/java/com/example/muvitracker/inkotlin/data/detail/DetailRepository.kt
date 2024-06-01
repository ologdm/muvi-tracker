package com.example.muvitracker.inkotlin.data.detail

import android.content.Context
import com.example.muvitracker.inkotlin.data.dto.DetailDto
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


class DetailRepository
private constructor(
    private val context: Context
) {
    val detailLocalDS = DetailLocalDS.getInstance(context)


    // GET
    fun getMovie(movieId: Int, callES: EmptyStatesCallback<DetailDto>) {
        var index = detailLocalDS.getItemIndex(movieId)

        if (index != -1) { // se trova index -> getDB
            getLocalItem(movieId, callES)
        } else {
            getNetworkItemAndAddToLocal(movieId, callES)
        }
    }


    // METODI PRIVATI
    private fun getNetworkItemAndAddToLocal(movieId: Int, callES: EmptyStatesCallback<DetailDto>) {
        callES.OnStart() // empty states
        OldDetaNetworkDS.callDetaServer(
            movieId,
            object : RetrofitCallback<DetailDto> {
                override fun onSuccess(serverItem: DetailDto) {
                    detailLocalDS.createItem(serverItem) // add DB
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


    private fun getLocalItem(movieId: Int, callES: EmptyStatesCallback<DetailDto>) {
        val databaseDto = detailLocalDS.readItem(movieId)
        callES.onSuccess(databaseDto)
    }

    // per viewmodel
    fun getLocalItem(movieId: Int): DetailDto {
        return detailLocalDS.readItem(movieId)
    }


    // ########################################################
    // SET FUNZIONI

    // OK
    fun toggleFavoriteOnDB(inputDto: DetailDto) {
        // !!! se l'ho aperto, e gia su DB !!!
        val dtoModificato = inputDto.copy(liked = !inputDto.liked)
        detailLocalDS.updateItem(dtoModificato)
        println("XXX_REPO_TOGGLE_UPDATE_DB")
    }

    // OK
    fun updateWatchedOnDB(inputDto: DetailDto) {
        // invio dto modificato
        detailLocalDS.updateItem(inputDto)
        println("XXX_DETAREPO_WATCHEDUPDATEDB")
    }


    companion object {
        private var instance: DetailRepository? = null
        fun getInstance(context: Context): DetailRepository {
            if (instance == null) {
                instance = DetailRepository(context)
            }
            return instance!!
        }
    }

}





