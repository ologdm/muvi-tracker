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
class BoxoViewModel @Inject constructor(
    private val moviesRepository: MoviesRepo
) : ViewModel() {

//    private val movieRepository = MoviesRepository.getInstance(application) - getInstance?


    fun getMovies(): LiveData<StateContainer<List<Movie>>> {
        return moviesRepository.getBoxoMovies().map { response ->
            when (response) {
                is IoResponse.Success -> {
                    StateContainer(
//                        isLoading = false,
                        data = response.dataValue
                    )
                }

                IoResponse.NetworkError -> {
                    StateContainer(
//                        isLoading = false,
                        isNetworkError = true,
                        data = moviesRepository.getBoxoCache()
                    )
                }

                IoResponse.OtherError -> {
                    StateContainer(
//                        isLoading = false,
                        isOtherError = true,
                        data = moviesRepository.getBoxoCache()
                    )
                }
            }
        }
    }

}
