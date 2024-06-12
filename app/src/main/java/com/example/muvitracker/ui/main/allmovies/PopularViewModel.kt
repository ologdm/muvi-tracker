package com.example.muvitracker.ui.main.allmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val movieRepository: MoviesRepo
) : ViewModel() {

    fun getMovies(): LiveData<StateContainer<List<Movie>>> {
        return movieRepository.getPopularMovies().map { response ->
            when (response) {
                is IoResponse.Success -> {
                    StateContainer(
//                        isLoading = false,
                        data = response.dataValue // state
                    )
                }

                IoResponse.NetworkError -> {
                    StateContainer(
//                        isLoading = false,
                        isNetworkError = true, // state
                        data = movieRepository.getPopularCache()
                    )
                }

                IoResponse.OtherError -> {
                    StateContainer(
//                        isLoading = false,
                        isOtherError = true, // state
                        data = movieRepository.getPopularCache()
                    )
                }
            }
        }
    }
}


