package com.example.muvitracker.ui.main.prefs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.domain.repo.PrefsMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO tutto
@HiltViewModel
class PrefsPersonViewmodel @Inject constructor(
    private val prefsMovieRepository: PrefsMovieRepository
) : ViewModel() {

    val prefsList = MutableLiveData<List<Movie>>()

    init {
        getPrefsList()
    }

    private fun getPrefsList (){
        viewModelScope.launch {
            prefsMovieRepository.getListFLow().collectLatest {
                prefsList.value = it
            }
        }
    }


    fun toggleLikedItem(movieId: Int) {
        viewModelScope.launch {
            prefsMovieRepository.toggleLikedOnDB(movieId) // bypass
        }
    }

    fun updateWatchedItem(updatedItem: Movie, watched: Boolean) {
        viewModelScope.launch {
            prefsMovieRepository.updateWatchedOnDB(updatedItem.ids.trakt, watched) // bypass
        }
    }

    fun deleteItem(movieId: Int) {
        viewModelScope.launch {
            prefsMovieRepository.deleteItemOnDB(movieId) // bypass
        }
    }

}