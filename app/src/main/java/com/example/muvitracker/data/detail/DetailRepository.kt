package com.example.muvitracker.data.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.data.prefs.PrefsLocalDS
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.combineLatest
import com.example.muvitracker.utils.concat
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
    ): LiveData<IoResponse<DetailMovie?>> {

        // 1. trova elemento in live - local detail e prefs | o return null
        val localLiveData = combineLatest(
            detailsLocalDS.getLivedataList(),
            prefsLocalDS.liveDataList, // TODO test
            combiner = { detailEntities, prefsEntities ->
                // trova elemento in detail se c'e, else null
                val movieEntity = detailEntities.find { detailEntity ->
                    detailEntity.ids.trakt == movieId
                }
                // trova elemento in prefs se c'e | else null
                val prefsEntity = prefsEntities.find { prefsEntity ->
                    prefsEntity.movieId == movieId
                }
                val detailMovie = movieEntity?.toDomain(prefsEntity)
                detailMovie //Livedata<DetailMovie>
            }).map { detailMovie ->   //Livedata<IoResponse<DetailMovie>>
            IoResponse.success(detailMovie)
        } // fine localLiveData


        // 2. livedata da internet
        val networkLivedata = MutableLiveData<IoResponse<DetailMovie?>>()
        getNetworkResultAndAddToLocal(movieId,
            onResponse = {
                networkLivedata.value = it
            })
        // 3.  combina 1 e 2 livedata , poi mostra il piu recente


        return concat(
            localLiveData,
            networkLivedata
        ).distinctUntilChanged() // leggi solo se cambiano valori
    }


    // network Call
    private fun getNetworkResultAndAddToLocal(
        movieId: Int,
        onResponse: (IoResponse<DetailMovie>) -> Unit
    ) {

        DetailNetworkDS.callDetailServer(
            movieId,
            onResponse = { retrofitResponse ->
                // 1
                val ioMapper = retrofitResponse.ioMapper { dto ->
                    val prefsList = prefsLocalDS.liveDataList.value // TODO test
                    val prefsEntity = prefsList?.find { dto.ids.trakt == it.movieId }
                    dto.toDomain(prefsEntity)

                }
                onResponse(ioMapper) // ##

                // 2. Success, aggiungi anche a db
                if (retrofitResponse is IoResponse.Success) {
                    detailsLocalDS.addOrUpdateItem(retrofitResponse.dataValue.toEntity())
                }
            }
        )

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



