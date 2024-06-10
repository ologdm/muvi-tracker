package com.example.muvitracker.ui.main.allmovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.data.movies.MoviesRepository
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer


class PopularViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    var stateContainer = MutableLiveData<StateContainer<Movie>>()
    var loading = MutableLiveData<StateContainer<Movie>>()
    private val repository = MoviesRepository.getInstance(application)


    // mettere dopo istanze altrimenti crasha
    init {
        loadMovies(isRefresh = false)
    }


    fun loadMovies(isRefresh: Boolean) {
        if (isRefresh) {
            stateContainer.value = StateContainer(isRefresh = true)
            println("XXX IS REFRESH")
        } else {
            stateContainer.value = StateContainer(isLoading = true)
            println("XXX LOADING")
        }


        repository.getPopularMovies { response ->
            when (response) {
                is IoResponse.Success -> {
                    stateContainer.value = StateContainer(dataList = response.dataValue)
                    println("XXX SUCCESS")
                }

                // ZZ TODO
                is IoResponse.NetworkError -> {
                    val cacheData = repository.getPopularCache()
                    stateContainer.value = if (cacheData.dataValue.isNotEmpty()) {
                        StateContainer(dataList = cacheData.dataValue, isNetworkError = true)
                    } else {
                        StateContainer(isNetworkError = true)
                    }
                    println("XXX NETWORK ERROR")
                }

                // ZZ TODO
                is IoResponse.OtherError -> {
                    val cacheData = repository.getPopularCache()
                    stateContainer.value = if (cacheData.dataValue.isNotEmpty()) {
                        StateContainer(dataList = cacheData.dataValue, isOtherError = true)
                    } else {
                        StateContainer(isOtherError = true)
                    }
                    println("XXX OTHER ERROR")
                }

                is IoResponse.Loading -> stateContainer.value = StateContainer(isLoading = true)
            }
        }
    }


//    fun loadMovies(isRefresh: Boolean) {
//        if (isRefresh) {
//            stateContainer.value = StateContainer(isRefresh = true)
//        } else {
//            stateContainer.value = StateContainer(isLoading = true)
//        }
//
//        repository.getPopularMovies { response ->
//            when (response) {
//                is IoResponse.Success -> {
//                    stateContainer.value = StateContainer(dataList = response.dataValue)
//                }
//
//                is IoResponse.NetworkError -> {
//                    stateContainer.value = StateContainer(isNetworkError = true)
//                }
//
//                is IoResponse.OtherError -> {
//                    stateContainer.value = StateContainer(isOtherError = true)
//                }
//            }
//        }
//    }

}


