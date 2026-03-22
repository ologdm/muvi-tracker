package com.example.presentation.prefs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Ids
import com.example.domain.model.Show
import com.example.domain.repo.DetailShowRepository
import com.example.domain.repo.PrefsShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefsShowsViewModel @Inject constructor(
    private val prefsShowRepository: PrefsShowRepository,
    private val detailShowRepository: DetailShowRepository
) : ViewModel() {

    val prefsList = MutableLiveData<List<Show>>()

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


    fun updateWatchedAllSingleShow(showIds: Ids, onComplete: () -> Unit) {
        viewModelScope.launch {
            // 1 start loading on adapter
            // 2 chiama funzione su repository - stessa di detail
            detailShowRepository.checkAndSetWatchedAllShowEpisodes(showIds)
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