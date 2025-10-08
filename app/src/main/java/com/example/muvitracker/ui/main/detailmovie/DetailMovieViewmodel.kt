package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.person.toDomain
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.domain.repo.PrefsMovieRepo
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
    private val detailMovieRepository: DetailMovieRepository,
    private val prefsMovieRepository: PrefsMovieRepo,
    private val traktApi: TraktApi
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
                            cachedMovie = response.dataValue
                            StateContainer(data = response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                // non printare come stringa
                                StateContainer(
                                    data = cachedMovie,
                                    isNetworkError = true
                                )
                            } else {
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


    // RELATED MOVIES
    fun loadRelatedMovies(movieId: Int) {
        viewModelScope.launch {
            detailMovieRepository.getRelatedMovies(movieId).ioMapper { movies ->
                relatedMoviesStatus.value = movies
            }
        }
    }


    // CAST ATTORI
    val castState = MutableLiveData<CastAndCrew>()

    fun loadCast(movieId: Int) {
        viewModelScope.launch {
            try {
                castState.value = traktApi.getAllMovieCast(movieId).toDomain()
            } catch (ex: Throwable) {
                ex.printStackTrace()
            }
        }
    }


}