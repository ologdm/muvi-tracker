package com.example.muvitracker.ui.main.prefs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.PrefsShowRepository
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.DetailShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefsShowViewModel @Inject constructor(
    private val prefsShowRepository: PrefsShowRepository
) : ViewModel() {

    val prefsList = MutableLiveData<List<DetailShow>>()

    init {
        getPrefsList()
    }

    // 00
    private fun getPrefsList (){
        viewModelScope.launch {
            prefsShowRepository.getListFLow().collectLatest {
                prefsList.value = it
            }
        }
    }

    // 000
    fun togglelikedShow(movieId: Int) {
        viewModelScope.launch {
            prefsShowRepository.toggleLikedOnDB(movieId) // bypass
            // aggiornare solo su prefsDao
        }
    }


    //
    fun updateWatchedItem(updatedItem: DetailMovie, watched: Boolean) {
        viewModelScope.launch {
//            prefsShowRepository.updateWatchedOnDB(updatedItem.ids.trakt, watched) // bypass
            // todo - aggiornare solo su episodesDao
        }
    }

    // 000 ok
    fun deleteItem(movieId: Int) {
        viewModelScope.launch {
            prefsShowRepository.deleteItemOnDB(movieId) // bypass
        }
    }

}