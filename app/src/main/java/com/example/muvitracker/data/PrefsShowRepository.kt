package com.example.muvitracker.data

import com.dropbox.android.external.store4.fresh
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.requests.ShowRequestKeys
import com.example.muvitracker.domain.model.DetailShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.singleOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsShowRepository @Inject constructor(
    private val detailShowRepository: DetailShowRepository,
    private val database: MyDatabase
) {

    private val prefsShowDao = database.prefsShowDao()
    private val detailShowDao = database.detailShowDao()
    private val episodeDao = database.episodesDao()


    // for Prefs Frgment todo
//    override - old
//    fun getListFLow(): Flow<List<DetailShow>> {
//        val detailListFLow = detailShowRepository.getDetailListFlow()
//        val prefsListFLow = prefsShowDao.readAll()
//
//        return detailListFLow
//            .combine(prefsListFLow) { detailList, prefsList ->
//                // mappa valori non nulli
//                prefsList.mapNotNull { prefsEntityR ->
//                    val detailItem = detailList.find { detailEntityR ->
//                        detailEntityR?.traktId == prefsEntityR?.traktId
//                    }
//                    detailItem?.toDomain(prefsEntityR)
//                }.sortedByDescending {
//                    it.addedDateTime
//                }
//            }
//    }


    // TODO
    fun getListFLow(): Flow<List<DetailShow>> {
        return prefsShowDao.getAllPrefsShows()
    }


    // LIKED OK
    // new or update
//    override
    suspend fun toggleLikedOnDB(id: Int) {
        // switch state on repository & update db
        val entity =
            prefsShowDao.readSingle(id) //  flow closing function
        if (entity != null) {
            prefsShowDao.toggleLiked(id)
        } else {
            prefsShowDao.insertSingle(
                PrefsShowEntity(
                    traktId = id,
                    liked = true,
//                    watchedAll = false,
//                    watchedCount = 0,
                    addedDateTime = System.currentTimeMillis()
                )
            )
        }
    }


    // TODO
//    override
    suspend fun deleteItemOnDB(id: Int) {
        prefsShowDao.deleteSingle(id)
    }


}


// WATCHED 000
// new or update
//    override
//    suspend fun updateWatchedOnDB(showId: Int) {
//        val entity = prefsShowDao.readSingle(showId)
//        println("TTTT: check entity trakt: ${entity?.traktId}") // arriva
//        if (entity == null) {
//            println("TTTT: new entity")
//            // NEW PREFS
//            prefsShowDao.insertSingle(
//                PrefsShowEntity(
//                    traktId = showId,
//                    liked = false,
//                    watchedAll = false,
//                    watchedCount = 1,  // firstElement Added
//                    addedDateTime = System.currentTimeMillis()
//                )
//            )
//        } else {
//            // UPDATE SHOW WATCHED ALL AND COUNT
//            val watchedEpisodes = episodeDao.checkWatchedEpisodesOfShow(showId).firstOrNull()?.size
//            val showTotalEpisodes =
//                detailShowDao.readSingleFlow(showId).firstOrNull()?.airedEpisodes
//            println("TTTT: watched-$watchedEpisodes, total-$showTotalEpisodes")
//
//            if (watchedEpisodes == showTotalEpisodes) { // max
//                println("TTTT: max")
//                prefsShowDao.updateWatched(
//                    showId = showId,
//                    watchedAll = true,
//                    watchedCount = watchedEpisodes ?: 0
//                )
//            } else { // not max
//                println("TTTT: non max")
//                prefsShowDao.updateWatched(
//                    showId = showId,
//                    watchedAll = false,
//                    watchedCount = watchedEpisodes ?: 0
//                )
//            }
//        }
//    }