package com.example.muvitracker.data.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.data.prefs.PrefsLocalDS
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.combineLatest
import com.example.muvitracker.utils.ioMapper


// NUOVA REPOSITORY

// koltin notes
//  list.find {condizione} -> restituisce Elemento or null

// livedata
// combineLast (1,2,combiner) -> return crea 3, ogni cambiam 1o2 3 cambierà di nuovo
// concat (1,2) -> return crea live di (1 o 2), viene mostrato il piu recente tra i 2


class DetailRepository
private constructor(
    private val context: Context
) {
    private val detailsLocalDS = DetailLocalDS.getInstance(context)
    private val prefsLocalDS = PrefsLocalDS.getInstance(context)


// GET ###########################################################################


    // 1. cerca su db se c'e - livedata
    // 2. cerca su network - livedata
    // 3. osserva i 2 valori concat()
    fun getDetailMovie(
        movieId: Int,
    ): LiveData<IoResponse<DetailMovie>> {

        // 1. trova elemento in live - local detail e prefs | o return null
        val localLiveData = combineLatest(
            detailsLocalDS.getLivedataList(),
            prefsLocalDS.getLivedataList(),
            combiner = { detailEntities, prefsEntities ->
                // trova elemento in detail se c'e, else null
                val movieEntity = detailEntities.find { detailEntity ->
                    movieId == detailEntity.ids.trakt
                }
                // trova elemento in prefs se c'e | else null
                val prefsEntity = prefsEntities.find { prefsEntity ->
                    movieId == prefsEntity.movieId
                }
                val detailMovie = movieEntity?.toDomain(prefsEntity)
                detailMovie //Livedata<DetailMovie>
            }).map { detailMovie ->   //Livedata<IoResponse<DetailMovie>>
//            // (dima)
//            val mapped = IoResponse.Success(detailMovie) as IoResponse<DetailMovie?>
//            mapped
            // (eugi)
            IoResponse.success(detailMovie)
        } // fine localLiveData


        // 2. livedata da internet
        val networkLivedata = MutableLiveData<IoResponse<DetailMovie>>()
        getNetworkResultAndAddToLocal(
            movieId,
            onResponse = {
                networkLivedata.value = it
            }
    }
    )

    // 3.  combina i due livedata e mostra il piu recente
    return
}




// network Call
//
private fun getNetworkResultAndAddToLocal(
    movieId: Int,
    onResponse: (IoResponse<DetailEntity>) -> Unit
) {



    DetailNetworkDS.callDetailServer(
        movieId,
        onResponse = { retrofitResponse ->
            val ioMapper = retrofitResponse.ioMapper { dto ->
                dto.toEntity()
            }
            onResponse(ioMapper) // ##

            // Success, aggiungi anche a db
            if (retrofitResponse is IoResponse.Success) {
                detailsLocalDS.addOrUpdateItem(retrofitResponse.dataValue.toEntity())
            }
        }
    )
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


////
//fun toggleFavoriteOnDB(inputDto: DetailDto) {
//    // !!! se l'ho aperto, e gia su DB !!!
//    val dtoModificato = inputDto.copy(liked = !inputDto.liked)
//    detailsLocalDS.updateItem(dtoModificato)
//}
//
////
//fun updateWatchedOnDB(inputDto: DetailDto) {
//    detailsLocalDS.updateItem(inputDto)        // invio dto modificato
//}


// viewmodel
//    // (per moodifica stato dto)
//    fun getMovieFromLocal(movieId: Int): DetailDto {
//        return detailsLocalDS.readItem(movieId)
//    }

//private fun getMovieFromLocal(
//        movieId: Int,
//        onResponse: (IoResponse<DetailDto>) -> Unit
//    ) {
//        val databaseDto = detailsLocalDS.readItem(movieId)
////        callES.onSuccess(databaseDto)
//        onResponse(IoResponse.Success(databaseDto))
//    }


