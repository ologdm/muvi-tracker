package com.example.muvitracker.inkotlin.mainactivity.prefs

import android.annotation.SuppressLint
import android.content.Context
import com.example.muvitracker.inkotlin.mainactivity.deta.repo.DetaRepo
import com.example.muvitracker.inkotlin.mainactivity.deta.repo.DLocalDS
import com.example.muvitracker.inkotlin.repo.dto.DetaDto


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


class PrefsRepo(
    val context: Context
) {

    companion object {

        // singleton
        @Volatile
        @SuppressLint("StaticFieldLeak")
        private var instance: PrefsRepo? = null


        fun getInstance(context: Context): PrefsRepo {
            instance ?: synchronized(this) {
                instance ?: PrefsRepo(context.applicationContext)
                    .also {
                        instance = it
                    }
            }
            return instance!!
        }

    }

    val detaLocalDS = DLocalDS.getInstance(context)
    val detaRepo = DetaRepo.getInstance(context)


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


}