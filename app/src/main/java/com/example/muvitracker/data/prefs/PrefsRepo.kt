package com.example.muvitracker.data.prefs

import android.content.Context
import com.example.muvitracker.data.detail.DetailLocalDS
import com.example.muvitracker.data.detail.DetailRepository
import com.example.muvitracker.data.dto.DetailDto



class PrefsRepo
private constructor(
    private val context: Context
) {

    private val detailLocalDS = DetailLocalDS.getInstance(context)
    private val oldDetaRepo = DetailRepository.getInstance(context)


    // GET
    fun filterPrefsFromDetails(): List<DetailDto> {

        var filteredList =
            detailLocalDS.loadListFromShared().filter {
                it.liked || it.watched
            }
        return filteredList
    }


    // SET
    fun toggleFavoriteOnDB(dtoToToggle: DetailDto) {
        oldDetaRepo.toggleFavoriteOnDB(dtoToToggle)
        // logica aggiornamento su detaRepo

        println("XXX_PREFS_REPO_LIKED")
    }


    fun updateWatchedOnDB(updatedDto: DetailDto) {
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