package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.domain.model.base.MovieBase
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.domain.repo.PrefsMovieRepository
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainerThree
import com.example.muvitracker.utils.StateContainerTwo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewmodel @Inject constructor(
    private val detailMovieRepository: DetailMovieRepository,
    private val prefsMovieRepository: PrefsMovieRepository,
    private val traktApi: TraktApi
) : ViewModel() {

    val detailState = MutableLiveData<StateContainerThree<Movie>>()

    // flow -> livedata
    fun loadMovieDetailFlow(movieId: Int) {
        var cachedMovie: Movie? = null

        viewModelScope.launch {
            detailMovieRepository.getSingleDetailMovieFlow(movieId)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            cachedMovie = response.dataValue
                            StateContainerThree(data = response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                // non printare come stringa
                                StateContainerThree(
                                    data = cachedMovie,
                                    isNetworkError = true
                                )
                            } else {
                                StateContainerThree(
                                    data = cachedMovie,
                                    isOtherError = true
                                )
                            }
                        }
                    }
                }
                .catch {
                    // flow no try catch, direttamente catch -
                    it.printStackTrace()
                }
                .collectLatest { container ->
                    detailState.value = container
                }
        }
    }


    // RELATED MOVIES --------------------------------------------------------------------------
    val relatedMoviesState = MutableLiveData<StateContainerTwo<List<MovieBase>>>()
    fun loadRelatedMovies(movieId: Int) {
        viewModelScope.launch {
            val response = detailMovieRepository.getRelatedMovies(movieId)
            when (response) {
                is IoResponse.Success -> {
                    relatedMoviesState.value = StateContainerTwo(response.dataValue, false)
                }

                is IoResponse.Error -> {
                    relatedMoviesState.value = StateContainerTwo(null, true)
                }
            }
        }
    }


    // CAST ATTORI ----------------------------------------------------------------------------
    val castState = MutableLiveData<StateContainerTwo<CastAndCrew>>()
    fun loadCast(movieId: Int) {
        viewModelScope.launch {
            val response = detailMovieRepository.getMovieCast(movieId)
            when (response){
                is IoResponse.Success -> {
                    castState.value = StateContainerTwo(response.dataValue, false)
                }

                is IoResponse.Error -> {
                    castState.value = StateContainerTwo(null, true)
                }
            }
        }
    }


    // SET (reactive get) ---------------------------------------------------------------------------
    fun toggleLikedMovie(movieId: Int) {
        viewModelScope.launch {
            prefsMovieRepository.toggleLikedOnDB(movieId)
        }
    }
    fun updateWatched(movieId: Int, watched: Boolean) {
        viewModelScope.launch {
            prefsMovieRepository.updateWatchedOnDB(movieId, watched)
        }

    }


}