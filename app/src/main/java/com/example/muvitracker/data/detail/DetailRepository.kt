package com.example.muvitracker.data.detail

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.combineLatest
import com.example.muvitracker.utils.ioMapper


// NUOVA REPOSITORY
// 1 getMovie  - combine (val db e val network) e osservali entrambi,
//
// 2 getNetwork + aggiorna db
// 3
// 4


class DetailRepository
private constructor(
    private val context: Context
) {
    private val detailsLocalDS = DetailLocalDS.getInstance(context)


    // GET ###########################################################################
    fun getDetailMovie(
        movieId: Int,
        onResponse: (IoResponse<DetailDto>) -> Unit
    ) {
        // cerca su db se c'e - livedata
        // cerca su network - livedata
        // osserva i 2 valori

        // trova elemento da 2 liste Live o return null
        val localLiveData = combineLatest(
            detailsLocalDS.getLivedataList(),
            detailsLocalDS.getLivedataList(),
            combiner = { detailEntities, prefsEntities ->
                val movie = detailEntities.find { detailEntity ->

                }

            })

    }


    // network
    private fun getMovieNetworkAndAddLocalDS(
        movieId: Int,
        onResponse: (IoResponse<DetailEntity>) -> Unit
    ) {

        DetailNetworkDS.callDetailServer(
            movieId,
            onResponse = { retrofitResponse ->

                val ioMapper = retrofitResponse.ioMapper { dto ->
                    dto.toEntity() // return (R type)
                }
                onResponse(ioMapper)
                //  NOonResponse(retrofitResponse)

                // success - save net data on db
                if (retrofitResponse is IoResponse.Success) {
                    detailsLocalDS.saveNewItemInSharedList(retrofitResponse.dataValue)
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
        val databaseDto = detailsLocalDS.readItem(movieId)
//        callES.onSuccess(databaseDto)
        onResponse(IoResponse.Success(databaseDto)) // TODO create new IoResponse
    }


// SET ###########################################################################

    // TODO ==
    fun toggleFavoriteOnDB(inputDto: DetailDto) {
        // !!! se l'ho aperto, e gia su DB !!!

    }

    // TODO ==
    fun updateWatchedOnDB(inputDto: DetailDto) {
        // invio dto modificato

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


//fun getDetsilMovie(
//    movieId: Int,
//    onResponse: (IoResponse<DetailDto>) -> Unit
//) {
//    var index = detailsLocalDS.getItemIndex(movieId)    // cerca index su Locale
//    if (index != -1) {                                // se trova index -> getDB
//        getMovieFromLocal(movieId, onResponse)
//    } else {
//        getMovieNetworkAndAddLocalDS(movieId, onResponse)
//    }
//}


//// TODO ==
//fun toggleFavoriteOnDB(inputDto: DetailDto) {
//    // !!! se l'ho aperto, e gia su DB !!!
//    val dtoModificato = inputDto.copy(liked = !inputDto.liked)
//    detailsLocalDS.updateItem(dtoModificato)
//}
//
//// TODO ==
//fun updateWatchedOnDB(inputDto: DetailDto) {
//    detailsLocalDS.updateItem(inputDto)        // invio dto modificato
//}


// viewmodel
//    // (per moodifica stato dto)
//    fun getMovieFromLocal(movieId: Int): DetailDto {
//        return detailsLocalDS.readItem(movieId)
//    }




