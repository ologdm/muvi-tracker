package com.example.muvitracker.mainactivity.kotlin.deta

import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.EmptyStatesCallback
import com.example.muvitracker.utils.kotlin.RetrofitCallbackK
import java.io.IOException



/**
 * repo gestisce logica flusso dati, non salva

 *
 * 1° GET FUNZIONI:
 * getMovie() - sceglie tra DB e Server,
 *  getLocalItem () - da DB
 *  getNetworkItemAndAddToLocal() - scarica e add a DB(success); gestisce empty states
 *
 *
 * 2° SET FUNZIONI:
 *
 *
 */


object DetaRepo {


    // istanze di local e server
    private val localDS = LocalDS
    private val networkDS = NetworkDS


    // ########################################################
    // GET FUNZIONI

    fun getMovie(movieId: Int, callES: EmptyStatesCallback): DetaDto {
        // 1 TODO: carica dati da shared in RAM (solo una volta)

        var index = localDS.getItemIndex(movieId)

        if (index != -1) { // se trova index -> getDB

            return getLocalItem(movieId)  // OK

            // TODO: aggiornare esistente

        } else {
            getNetworkItemAndAddToLocal(movieId, callES)

            return getLocalItem(movieId)
        }


    }




    // METODI PRIVATI
    // OK
    private fun getNetworkItemAndAddToLocal(movieId: Int, callES: EmptyStatesCallback) {
        callES.OnStart() // empty states

        networkDS.callDetaServer(movieId, object : RetrofitCallbackK<DetaDto> {

            override fun onSuccess(serverItem: DetaDto) {
                localDS.createItem(serverItem) // add DB

                callES.onSuccess() // empty states
            }

            override fun onError(throwable: Throwable) {
                if (throwable is IOException)
                    callES.onErrorIO() // empty states
                else
                    callES.onErrorOther() // empty states

            }
        })
    }

    // OK
    fun getLocalItem (movieId: Int): DetaDto {
        return localDS.readItem(movieId)
    }


    // ########################################################
    // SET FUNZIONI

    // OK
    fun toggleFavoriteOnDB (inputDto :DetaDto){
        // >> se l'ho aperto, e gia su DB

        // inversione stato
        inputDto.liked = !inputDto.liked

        // db
        localDS.updateItem(inputDto) // db poi crea copia
    }

    // OK
    fun updateWatchedOnDB (inputDto: DetaDto){

        localDS.updateItem(inputDto) // db poi crea copia

    }










}




// repo -> decide da dove prendere info e inviarla al fragment
//   - locale shared prefs poi scarica e aggiorna
//   - scarica da server direttamente



