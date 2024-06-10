package com.example.muvitracker.ui.main.allmovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.muvitracker.data.movies.MoviesRepository
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer


class PopularViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val movieRepository = MoviesRepository.getInstance(application)

    //    var stateContainer = MutableLiveData<StateContainer<Movie>>()
    var loading = MutableLiveData<Boolean>()


    fun getMovies(isRefresh: Boolean): LiveData<StateContainer<List<Movie>>> {
        loading.value = !isRefresh // if refresh, no loading bar

        return movieRepository.getPopularMovies().map { response ->
            when (response) {
                is IoResponse.Success -> {
                    loading.value = false
                    StateContainer(data = response.dataValue)
                }

                IoResponse.NetworkError -> {
                    loading.value = false
                    StateContainer(
                        isNetworkError = true,
                        data = movieRepository.getPopularCache()
                    )
                }

                IoResponse.OtherError -> {
                    loading.value = false
                    StateContainer(isOtherError = true)
                }
            }
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


//    return movieRepository.getPopularMovies()
//    .map { response ->
//        when (response) {
//            is IoResponse.Success -> {
//                StateContainer(dataList = response.dataValue)
//            }
//
//            IoResponse.NetworkError -> {StateContainer(isNetworkError = true)}
//            IoResponse.OtherError -> {StateContainer(isOtherError = true)}
//        }
//    }
//}


