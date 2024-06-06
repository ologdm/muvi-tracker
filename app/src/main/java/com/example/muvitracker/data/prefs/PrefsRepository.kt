package com.example.muvitracker.data.prefs

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.muvitracker.data.detail.DetailLocalDS
import com.example.muvitracker.data.detail.DetailRepository
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.domain.model.DetailMovie


class PrefsRepository(
    private val context: Context
) {
    private val detailRepository = DetailRepository.getInstance(context)


    private val detailLocalDS = DetailLocalDS.getInstance(context)
    private val prefsLocalDS = PrefsLocalDS.getInstance(context)


    // GET TODO
    fun getPrefsMovies()
            : LiveData<List<DetailMovie>> {

        // combinare prefsEntity e detailEntity dai due local

        return TODO()
    }













    // SET ###################################################### TODO
    fun toggleFavoriteOnDB(dtoToToggle: DetailDto) {
        detailRepository.toggleFavoriteOnDB(dtoToToggle)
        // logica aggiornamento su detaRepo
        println("XXX_PREFS_REPO_LIKED")
    }

    fun updateWatchedOnDB(updatedDto: DetailDto) {
        detailRepository.updateWatchedOnDB(updatedDto)
        // solo agigornamento db
        println("XXX_PREFSREPO_WATCHED")
    }

    // singleton
    companion object {
        private var instance: PrefsRepository? = null

        fun getInstance(context: Context): PrefsRepository {
            if (instance == null) {
                instance = PrefsRepository(context)
            }
            return instance!!
        }
    }
}

// GET
fun filterPrefsFromDetails(): List<DetailDto> {

    var filteredList =
        detailLocalDS.loadListFromShared().filter {
            it.liked || it.watched
        }
    return filteredList
}