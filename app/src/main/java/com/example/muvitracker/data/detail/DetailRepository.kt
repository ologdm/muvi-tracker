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


class DetailRepository
private constructor(
    private val context: Context
) {
    private val detailsLocalDS = DetailLocalDS.getInstance(context)
    private val prefsLocalDS = PrefsLocalDS.getInstance(context)


// GET ###########################################################################

    // 1. search on db - livedata
    // 2. search on network - livedata
    // 3. observe the 2 values - concat()
    fun getDetailMovie(
        movieId: Int,
    ): LiveData<IoResponse<DetailMovie?>> {

        // 1. finds element in live - local detail and prefs | or return null
        val localLiveData = combineLatest(
            detailsLocalDS.getLivedataList(),
            prefsLocalDS.liveDataList,
            combiner = { detailEntities, prefsEntities ->
                // find element in detail - if there is, else null
                val movieEntity = detailEntities.find { detailEntity ->
                    detailEntity.ids.trakt == movieId
                }
                // find element in prefs if there is | else null
                val prefsEntity = prefsEntities.find { prefsEntity ->
                    prefsEntity.movieId == movieId
                }
                val detailMovie = movieEntity?.toDomain(prefsEntity)
                detailMovie //Livedata<DetailMovie>
            }).map { detailMovie ->   //Livedata<IoResponse<DetailMovie>>
            IoResponse.success(detailMovie)
        } // fine localLiveData


        // 2. network livedata
        val networkLivedata = MutableLiveData<IoResponse<DetailMovie?>>()
        getNetworkResultAndAddToLocal(movieId,
            onResponse = {
                networkLivedata.value = it
            })

        // 3. combine 1 and 2 livedata, then show the most recent one
        return concat(
            localLiveData,
            networkLivedata
        ).distinctUntilChanged() // read only if values change
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

                // 2. Success, add also on db
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



