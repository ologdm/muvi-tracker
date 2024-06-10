package com.example.muvitracker.ui.main.allmovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.data.movies.MoviesRepository
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer


class BoxoViewModel(
    private val application: Application,
) : AndroidViewModel(application) {

    private val repository = MoviesRepository.getInstance(application)
    val stateContainer = MutableLiveData<StateContainer<Movie>>()

    // mettere dopo istanze
    init {
        loadMovies(isRefresh = false)
    }


    fun loadMovies(isRefresh: Boolean) {
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
