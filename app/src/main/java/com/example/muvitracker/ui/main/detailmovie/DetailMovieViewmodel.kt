package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.imagetmdb.TmdbRepository
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.domain.repo.PrefsRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import com.example.muvitracker.utils.ioMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewmodel @Inject constructor(
    private val detailMovieRepository: DetailRepo,
    private val prefsRepository: PrefsRepo,
) : ViewModel() {

    val detailState = MutableLiveData<StateContainer<DetailMovie>>()
    val relatedMoviesStatus = MutableLiveData<List<Movie>>()

    // flow -> livedata
    fun loadMovieDetailFlow(movieId: Int) {
        var cachedMovie: DetailMovie? = null

        viewModelScope.launch {
            detailMovieRepository.getSingleDetailMovieFlow(movieId)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            println("ZZZ_VM_S${response.dataValue}")
                            cachedMovie = response.dataValue
                            StateContainer(data = response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                println("ZZZ_VM_E1${response.t}")
                                // non printare come stringa
                                StateContainer(
                                    data = cachedMovie,
                                    isNetworkError = true
                                )
                            } else {
                                println("ZZZ_VM_E2${response.t}")
                                StateContainer(
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


    // SET
    fun toggleLikedMovie(id: Int) {
        viewModelScope.launch {
            prefsRepository.toggleLikedOnDB(id)
        }
    }


    fun updateWatched(id: Int, watched: Boolean) {
        viewModelScope.launch {
            prefsRepository.updateWatchedOnDB(id, watched)
        }

    }



    // RELATED MOVIES
    fun loadRelatedMovies(movieId: Int) {
        viewModelScope.launch {
            detailMovieRepository.getRelatedMovies(movieId).ioMapper {movies->
                relatedMoviesStatus.value = movies
            }
        }

    }


}