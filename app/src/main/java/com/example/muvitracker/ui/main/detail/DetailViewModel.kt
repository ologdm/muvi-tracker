package com.example.muvitracker.ui.main.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.muvitracker.data.detail.DetailRepository
import com.example.muvitracker.data.prefs.PrefsRepository
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository,
    private val prefsRepository: PrefsRepository
) : ViewModel() {


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
                }
            }
    }


    fun toggleFavorite(id: Int) {
        prefsRepository.toggleFavoriteOnDB(id)
    }


    fun updateWatched(id: Int, watched: Boolean) {
        prefsRepository.updateWatchedOnDB(id, watched)
    }


}

