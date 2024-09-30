package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.imagetmdb.TmdbRepository
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.domain.repo.PrefsRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewmodel @Inject constructor(
    private val detailRepository: DetailRepo,
    private val prefsRepository: PrefsRepo,
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    val detailState = MutableLiveData<StateContainer<DetailMovie>>()

    // flow -> livedata
    fun getStateContainer(movieId: Int) {
        var cachedMovie: DetailMovie? = null

        viewModelScope.launch {
            detailRepository.getSingleDetailMovieFlow(movieId)
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
    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            prefsRepository.toggleLikedOnDB(id)
        }
    }


    fun updateWatched(id: Int, watched: Boolean) {
        viewModelScope.launch {
            prefsRepository.updateWatchedOnDB(id, watched)
        }

    }


    // IMAGES TMDB todo
    val backdropImageUrl = MutableLiveData<String>()
    val posterImageUrl = MutableLiveData<String>()

    fun loadImageMovieTest(movieTmdbId: Int) {
        viewModelScope.launch {
            val x = tmdbRepository.getQuickPathForMovie(movieTmdbId)
            backdropImageUrl.value = "https://image.tmdb.org/t/p/original${x.backdropPath.toString()}"
            posterImageUrl.value = "https://image.tmdb.org/t/p/original${x.posterPath.toString()}"
        }
    }


}