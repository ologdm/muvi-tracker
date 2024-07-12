package com.example.muvitracker.data.prefs

import com.example.muvitracker.data.detail.DetailRepositoryTest
import com.example.muvitracker.data.detail.toDomain
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.PrefsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsRepository @Inject constructor(
    private val prefsLocalDS: PrefsLocalDS,
    private val detailRepositoryTest: DetailRepositoryTest
) : PrefsRepo {


    // GET ######################################################
    override fun getListFLow(): Flow<List<DetailMovie>> {
        val detailsList = detailRepositoryTest.getDetailListFlow()
        val prefsList = prefsLocalDS.getPrefsListFlow()

        return detailsList
            .combine(prefsList) { detailList, prefsList ->
                prefsList.mapNotNull { prefsEntity ->
                    val detailItem = detailList.find { detailEntity ->
                        detailEntity.ids.trakt == prefsEntity.movieId
                    }
                    detailItem?.toDomain(prefsEntity)
                }
            }
    }



    // SET ######################################################
    override fun toggleFavoriteOnDB(id: Int) {
        // switch state on db
        prefsLocalDS.toggleFavoriteOnDB(id) // bypass
    }


    override fun updateWatchedOnDB(id: Int, watched: Boolean) {
        // only update on db
        prefsLocalDS.updateWatchedOnDB(id, watched) // bypass
    }


    override fun deleteItemOnDB(movieId: Int) {
        prefsLocalDS.deleteItemFromDB(movieId) // bypass
    }


}
