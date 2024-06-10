package com.example.muvitracker.ui.main.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.muvitracker.data.detail.DetailRepository
import com.example.muvitracker.data.prefs.PrefsRepository
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer


class DetailViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val detailRepository = DetailRepository.getInstance(application)
    private val prefsRepository = PrefsRepository.getInstance(application)


// GET ###############################################################

    fun getStateContainer(movieId: Int): LiveData<StateContainer<DetailMovie>> {
        return detailRepository.getDetailMovie(movieId)
            .map { repoRespopnse ->
                when (repoRespopnse) {
                    is IoResponse.Success -> {
                        StateContainer(data = repoRespopnse.dataValue)
                    }

                    is IoResponse.NetworkError -> {
                        StateContainer(isNetworkError = true)
                    }

                    is IoResponse.OtherError -> {
                        StateContainer(isOtherError = true)
                    }

                    IoResponse.Loading -> TODO()
                }
            }
    }


    // SET ############################################################
    fun toggleFavorite(id: Int) {
        prefsRepository.toggleFavoriteOnDB(id)
    }

    fun updateWatched(id: Int, watched: Boolean) {
        prefsRepository.updateWatchedOnDB(id, watched)
    }


}

