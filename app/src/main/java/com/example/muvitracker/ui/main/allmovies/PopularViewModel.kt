package com.example.muvitracker.ui.main.allmovies

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.muvitracker.data.movies.MoviesRepository
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val movieRepository: MoviesRepository
) : ViewModel() {

//    private val movieRepository = MoviesRepository.getInstance(application)


    fun getMovies(): LiveData<StateContainer<List<Movie>>> {
        return movieRepository.getPopularMovies().map { response ->
            when (response) {
                is IoResponse.Success -> {
                    StateContainer(
                        isLoading = false,
                        data = response.dataValue
                    )
                }

                IoResponse.NetworkError -> {
                    StateContainer(
                        isLoading = false,
                        isNetworkError = true,
                        data = movieRepository.getPopularCache()
                    )
                }

                IoResponse.OtherError -> {
                    StateContainer(
                        isLoading = false,
                        isOtherError = true,
                        data = movieRepository.getPopularCache()
                    )
                }
            }
        }
    }
}


