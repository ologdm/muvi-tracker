package com.example.muvitracker.ui.main.prefs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.PrefsRepo
import com.example.muvitracker.utils.StateContainer
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


    fun toggleFavoriteItem(itemToToggle: DetailMovie) {
        viewModelScope.launch {
            prefsRepository.toggleFavoriteOnDB(itemToToggle.ids.trakt) // bypass
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