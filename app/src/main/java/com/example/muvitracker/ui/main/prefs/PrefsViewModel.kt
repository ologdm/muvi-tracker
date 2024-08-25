package com.example.muvitracker.ui.main.prefs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.PrefsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefsViewModel @Inject constructor(
    private val prefsRepository: PrefsRepo
) : ViewModel() {

    val prefsList = MutableLiveData<List<DetailMovie>>()

    init {
        getPrefs()
    }

    private fun getPrefs (){
        viewModelScope.launch {
            prefsRepository.getListFLow().collectLatest {
                prefsList.value = it
            }
        }
    }


    fun toggleFavoriteItem(movieId: Int) {
        viewModelScope.launch {
            prefsRepository.toggleFavoriteOnDB(movieId) // bypass
        }
    }

    fun updateWatchedItem(updatedItem: DetailMovie, watched: Boolean) {
        viewModelScope.launch {
            prefsRepository.updateWatchedOnDB(updatedItem.ids.trakt, watched) // bypass
        }
    }

    fun deleteItem(movieId: Int) {
        viewModelScope.launch {
            prefsRepository.deleteItemOnDB(movieId) // bypass
        }
    }

}