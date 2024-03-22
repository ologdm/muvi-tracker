package com.example.muvitracker.mainactivity.kotlin.prefs

import com.example.muvitracker.mainactivity.kotlin.deta.DetaRepo
import com.example.muvitracker.mainactivity.kotlin.deta.xDetaLocalDS
import com.example.muvitracker.repo.kotlin.dto.DetaDto


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


object PrefsRepo {

    // GET
    fun filterPrefsFromDetails(): List<DetaDto> {

        var filteredList = xDetaLocalDS
            .loadListFromShared()
            .filter {
                it.liked || it.watched

            }
        return filteredList
    }


    // SET

    fun toggleFavoriteOnDB(dtoToToggle: DetaDto) {
        DetaRepo.toggleFavoriteOnDB(dtoToToggle)
        // logica aggiornamento su detaRepo

        println("XXX_PREFS_REPO_LIKED")
    }


    fun updateWatchedOnDB(updatedDto: DetaDto) {
        DetaRepo.toggleFavoriteOnDB(updatedDto)
        // solo agigornamento db

        println("XXX_PREFS_REPO_WATCHED")
    }


}