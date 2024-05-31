package com.example.muvitracker.inkotlin.data.prefs

import android.content.Context
import com.example.muvitracker.inkotlin.data.details.OldDetaRepo
import com.example.muvitracker.inkotlin.data.details.DetaLocalDS
import com.example.muvitracker.inkotlin.data.dto.DetaDto


/**
 * GET
 * - filterPrefsFromDetails()
 *                  > filtra elementi da DetailsDB
 *
 * SET
 * - toggleFavoriteOnDB
 * - updateWatchedOnDB
 *
 */


class PrefsRepo
private constructor(
    private val context: Context
) {

    private val detaLocalDS = DetaLocalDS.getInstance(context)
    private val oldDetaRepo = OldDetaRepo.getInstance(context)


    // GET
    fun filterPrefsFromDetails(): List<DetaDto> {

        var filteredList =
            detaLocalDS.loadListFromShared().filter {
                it.liked || it.watched
            }
        return filteredList
    }


    // SET
    fun toggleFavoriteOnDB(dtoToToggle: DetaDto) {
        oldDetaRepo.toggleFavoriteOnDB(dtoToToggle)
        // logica aggiornamento su detaRepo

        println("XXX_PREFS_REPO_LIKED")
    }


    fun updateWatchedOnDB(updatedDto: DetaDto) {
        oldDetaRepo.updateWatchedOnDB(updatedDto)
        // solo agigornamento db

        println("XXX_PREFSREPO_WATCHED")
    }

    // singleton
    companion object {
        private var instance: PrefsRepo? = null

        fun getInstance(context: Context): PrefsRepo {
            if (instance == null) {
                instance = PrefsRepo(context)
            }
            return instance!!
        }
    }
}