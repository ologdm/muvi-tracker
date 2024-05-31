package com.example.muvitracker.inkotlin.ui.main.allmovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.inkotlin.data.movies.MoviesRepository
import com.example.muvitracker.inkotlin.domain.model.MovieModel
import com.example.muvitracker.inkotlin.utils.IoResponse
import com.example.muvitracker.inkotlin.utils.StateContainer

class PopuViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    var stateContainer = MutableLiveData<StateContainer<MovieModel>>()
    private val repository = MoviesRepository.getInstance(application)



    // mettere dopo istanze altrimenti crasha
    init {
        loadMovies(isRefresh = false)
    }


    fun loadMovies(isRefresh: Boolean) {
        if (isRefresh) {
            stateContainer.value = StateContainer(isRefresh = true)
        } else {
            stateContainer.value = StateContainer(isLoading = true)
        }

        repository.getPopularMovies { response ->
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