package com.example.muvitracker.inkotlin.ui.main.allmovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.inkotlin.data.movies.MoviesRepository
import com.example.muvitracker.inkotlin.domain.model.MovieModel
import com.example.muvitracker.inkotlin.utils.IoResponse
import com.example.muvitracker.inkotlin.utils.StateContainer


class BoxoViewModel(
    private val application: Application,
) : AndroidViewModel(application) {

    private val repository = MoviesRepository.getInstance(application)
    val stateContainer = MutableLiveData<StateContainer<MovieModel>>()

    // mettere dopo istanze
    init {
        loadMovies(isRefresh = false)
    }


    fun loadMovies(isRefresh: Boolean) {
        if (isRefresh) {
            stateContainer.value = StateContainer(isRefresh = true)
        } else {
            stateContainer.value = StateContainer(isLoading = true) // si puo eliminare?
        }
        repository.getBoxoMovies { response ->
            when (response) {
                is IoResponse.Success -> {
                    stateContainer.value = StateContainer(dataList = response.dataValue)
                }

                is IoResponse.NetworkError -> {
                    stateContainer.value = StateContainer(isNetworkError = true)
                }

                is IoResponse.OtherError -> {
                    stateContainer.value = StateContainer(isOtherError = true)
                }
            }
        }
    }

}
