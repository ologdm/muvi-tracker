package com.example.muvitracker.data.detail

import android.content.Context
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.utils.IoResponse


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
            getMovieFromLocal(movieId, onResponse)
        } else {
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





