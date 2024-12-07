package com.example.muvitracker.ui.main.prefs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.PrefsMovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefsMovieViewModel @Inject constructor(
    private val prefsMovieRepository: PrefsMovieRepo
) : ViewModel() {

    val prefsList = MutableLiveData<List<DetailMovie>>()

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

    fun updateWatchedItem(updatedItem: DetailMovie, watched: Boolean) {
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