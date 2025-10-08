package com.example.muvitracker.ui.main.prefs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.domain.repo.DetailShowRepository
import com.example.muvitracker.domain.repo.PrefsShowRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefsShowViewModel @Inject constructor(
    private val prefsShowRepository: PrefsShowRepo,
    private val detailShowRepository: DetailShowRepository
) : ViewModel() {

    val prefsList = MutableLiveData<List<DetailShow>>()

    init {
        getPrefsList()
    }

    // 00
    private fun getPrefsList() {
        viewModelScope.launch {
            prefsShowRepository.getListFLow().collectLatest {
                prefsList.value = it
            }
        }
    }

    // 000
    fun toggleLikedShow(movieId: Int) {
        viewModelScope.launch {
            prefsShowRepository.toggleLikedOnDB(movieId) // bypass
            // aggiornare solo su prefsDao
        }
    }


    fun updateWatchedAllSingleShow(showId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            // 1 start loading on adapter
            // 2 chiama funzione su repository - stessa di detail
            detailShowRepository.checkAndSetWatchedAllShowEpisodes(showId)
            // 3 finish
            onComplete()
        }
    }

    // 000 ok
    fun deleteItem(movieId: Int) {
        viewModelScope.launch {
            prefsShowRepository.deleteItemOnDB(movieId) // bypass
        }
    }

}