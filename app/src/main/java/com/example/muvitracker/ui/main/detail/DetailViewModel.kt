package com.example.muvitracker.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.domain.repo.PrefsRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepo,
    private val prefsRepository: PrefsRepo
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

