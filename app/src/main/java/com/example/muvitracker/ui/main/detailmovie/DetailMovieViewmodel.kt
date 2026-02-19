package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.CastMember
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.domain.model.Provider
import com.example.muvitracker.domain.model.base.MovieBase
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.domain.repo.PrefsMovieRepository
import com.example.muvitracker.ui.main.providers.AppCountry
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ListStateContainerTwo
import com.example.muvitracker.utils.StateContainerThree
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewmodel @Inject constructor(
    private val detailMovieRepository: DetailMovieRepository,
    private val prefsMovieRepository: PrefsMovieRepository,
    private val traktApi: TraktApi
) : ViewModel() {

    val detailState = MutableLiveData<StateContainerThree<Movie>>()

    val relatedMoviesState = MutableLiveData<ListStateContainerTwo<MovieBase>>()
    val castState = MutableLiveData<ListStateContainerTwo<CastMember>>()
    // 1.1.4 providers ok
    val providersState = MutableStateFlow<List<Provider>>(emptyList())

    private var movieNotes = ""

    // valore iniziale enum
    var countryEnum = AppCountry.fromCode(Locale.getDefault().country.lowercase())


    // flow -> livedata
    fun loadMovieDetailFlow(movieIds: Ids) {
        var cachedMovie: Movie? = null

        viewModelScope.launch {
            detailMovieRepository.getSingleDetailMovieFlow(movieIds)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            cachedMovie = response.dataValue
                            movieNotes = response.dataValue.notes
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
    fun loadRelatedMovies(movieId: Int) {
        // senza store, no problema se first emission emptyList
        viewModelScope.launch {
            val response = detailMovieRepository.getRelatedMovies(movieId)
            // for test
//            delay(2000)
            when (response) {
                is IoResponse.Success -> {
                    relatedMoviesState.value = ListStateContainerTwo(response.dataValue, false)
                }

                is IoResponse.Error -> {
                    relatedMoviesState.value = ListStateContainerTwo(emptyList(), true)
                }
            }
        }
    }


    // CAST ATTORI ----------------------------------------------------------------------------
    fun loadCast(movieId: Int) {
        // senza store, no problema se first emission emptyList
        viewModelScope.launch {
            val response = detailMovieRepository.getMovieCast(movieId)
            // for test
//            delay(2000)
            when (response) {
                is IoResponse.Success -> {
                    // !! List<CastMember> -> (dto->domain salvata come emptyList)
                    if (response.dataValue.castMembers.isNotEmpty()) {
                        castState.value =
                            ListStateContainerTwo(response.dataValue.castMembers, false)
                    } else {
                        castState.value = ListStateContainerTwo(emptyList(), false)
                    }
                }

                is IoResponse.Error -> {
                    castState.value = ListStateContainerTwo(emptyList(), true)
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

    fun setNotes(movieId: Int, notes: String) {
        viewModelScope.launch {
            prefsMovieRepository.setNotesOnDB(movieId, notes)
            movieNotes = notes
        }
    }


    fun getNotes(): String = movieNotes


    // 1.1.4 OK
    // senza loading

    fun loadProviders(movieId: Int) {
        // TODO:  aggiornare
        viewModelScope.launch {
            // devo chiamare la suspended fun
            val response = detailMovieRepository.getMovieProviders(movieId)

            when(response){
                is IoResponse.Success -> {
                    providersState.value = response.dataValue
                }

                is IoResponse.Error -> {
                    response.t.printStackTrace()
                }
            }

        }


    }



}