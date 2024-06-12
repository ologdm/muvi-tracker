package com.example.muvitracker.data.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.data.prefs.PrefsLocalDS
import com.example.muvitracker.data.startNetworkCall
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.combineLatest
import com.example.muvitracker.utils.concat
import com.example.muvitracker.utils.ioMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val detailLocalDS: DetailLocalDS,
    private val prefsLocalDS: PrefsLocalDS,
    private val traktApi: TraktApi
) :DetailRepo {

    // GET ###########################################################################
    override fun getDetailMovie(movieId: Int): LiveData<IoResponse<DetailMovie?>> {
        val localLiveData = combineLatest(
            detailLocalDS.getLivedataList(),
            prefsLocalDS.liveDataList,
            combiner = { detailEntities, prefsEntities ->
                val movieEntity = detailEntities.find { detailEntity ->
                    detailEntity.ids.trakt == movieId
                }
                val prefsEntity = prefsEntities.find { prefsEntity ->
                    prefsEntity.movieId == movieId
                }
                val detailMovie = movieEntity?.toDomain(prefsEntity)
                detailMovie
            }).map { detailMovie ->
            IoResponse.success(detailMovie)
        }

        val networkLivedata = MutableLiveData<IoResponse<DetailMovie?>>()
        getNetworkResultAndAddToLocal(movieId,
            onResponse = {
                networkLivedata.value = it
            })

        return concat( // combine 1 and 2 livedata, then show the most recent one
            localLiveData,
            networkLivedata
        ).distinctUntilChanged() // read only if values change
    }


    private fun getNetworkResultAndAddToLocal(
        movieId: Int,
        onResponse: (IoResponse<DetailMovie>) -> Unit
    ) {
        // 1
        traktApi.getMovieDetails(movieId)
            .startNetworkCall { retrofitResponse ->
                val ioMapper = retrofitResponse.ioMapper { dto ->
                    val prefsList = prefsLocalDS.liveDataList.value
                    val prefsEntity = prefsList?.find { dto.ids.trakt == it.movieId }
                    dto.toDomain(prefsEntity)
                }
                onResponse(ioMapper)

                // 2. Success, add also on db
                if (retrofitResponse is IoResponse.Success) {
                    detailLocalDS.addOrUpdateItem(retrofitResponse.dataValue.toEntity())
                }
            }
    }

}








