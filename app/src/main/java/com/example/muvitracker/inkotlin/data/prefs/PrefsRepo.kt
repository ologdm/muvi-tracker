package com.example.muvitracker.inkotlin.data.prefs

import android.annotation.SuppressLint
import android.content.Context
import com.example.muvitracker.inkotlin.data.details.DetaRepo
import com.example.muvitracker.inkotlin.data.details.DLocalDS
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

    private val detaLocalDS = DLocalDS.getInstance(context)
    private val detaRepo = DetaRepo.getInstance(context)


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
        detaRepo.toggleFavoriteOnDB(dtoToToggle)
        // logica aggiornamento su detaRepo

        println("XXX_PREFS_REPO_LIKED")
    }


    fun updateWatchedOnDB(updatedDto: DetaDto) {
        detaRepo.updateWatchedOnDB(updatedDto)
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