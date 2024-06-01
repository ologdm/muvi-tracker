package com.example.muvitracker.inkotlin.data.detail

import android.content.Context
import com.example.muvitracker.inkotlin.data.dto.DetailDto
import com.example.muvitracker.inkotlin.utils.IoResponse

/**
 * - repo gestisce logica flusso dati, non salva
 * - i film gli salvo solo la prima volta, no aggiornamento
 *
 *
 *  1° GET FUNZIONI:
 *
 *  -getMovie() OK
 *      > sceglie tra DB o Server
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

// TODO giugno
//  1. IoResponse, Container
//  2. domain
// -
// -

class DetailRepository
private constructor(
    private val context: Context
) {

    private val localDS = DetailLocalDS.getInstance(context)


    // GET
    // TODO IoResponse
    fun getMovie(
        movieId: Int,
        onResponse: (IoResponse<DetailDto>) -> Unit
    ) {
        // ==
        var index = localDS.getItemIndex(movieId)    // cerca index su Locale

        if (index != -1) {                                // se trova index -> getDB
//            getLocalItem(movieId, callES)
            getMovieFromLocal(movieId, onResponse)
        } else {
//            getNetworkItemAndAddToLocal(movieId, callES)
            getMovieNetworkAndAddLocalDS(movieId, onResponse)
        }
    }


    // network
    // TODO IoResponse
    private fun getMovieNetworkAndAddLocalDS(
        movieId: Int,
        onResponse: (IoResponse<DetailDto>) -> Unit
    ) {

        DetailNetworkDS.callDetailServer(
            movieId,
            onResponse = { retrofitResponse ->
                // 1. data bypass
                onResponse(retrofitResponse)

                // success - save net data on db
                if (retrofitResponse is IoResponse.Success){
                    localDS.createItem(retrofitResponse.dataValue)
                }
            }
        )
    }


    // interno
    // TODO IoResponse OK
    private fun getMovieFromLocal(
        movieId: Int,
        onResponse: (IoResponse<DetailDto>) -> Unit
    ) {
        val databaseDto = localDS.readItem(movieId)
//        callES.onSuccess(databaseDto)
        onResponse(IoResponse.Success(databaseDto)) // TODO create new IoResponse
    }


    // viewmodel
    // (per moodifica stato dto)
    fun getMovieFromLocal(movieId: Int): DetailDto {
        return localDS.readItem(movieId)
    }


    // ########################################################
    // SET FUNZIONI
    // TODO ==
    fun toggleFavoriteOnDB(inputDto: DetailDto) {
        // !!! se l'ho aperto, e gia su DB !!!
        val dtoModificato = inputDto.copy(liked = !inputDto.liked)
        localDS.updateItem(dtoModificato)
    }

    // TODO ==
    fun updateWatchedOnDB(inputDto: DetailDto) {
        localDS.updateItem(inputDto)        // invio dto modificato
    }


    // SINGLETON OK
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





