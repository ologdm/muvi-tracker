package com.example.muvitracker.ui.main.prefs

import androidx.lifecycle.ViewModel
import com.example.muvitracker.data.prefs.PrefsRepository
import com.example.muvitracker.domain.model.DetailMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrefsViewModel @Inject constructor(
    private val prefsRepository: PrefsRepository
) : ViewModel() {

    val prefsList = prefsRepository.getList()


    fun toggleFovoriteItem(itemToToggle: DetailMovie) {
        prefsRepository.toggleFavoriteOnDB(itemToToggle.ids.trakt) // bypass
    }

    fun updateWatchedItem(updatedItem: DetailMovie, watched: Boolean) {
        prefsRepository.updateWatchedOnDB(updatedItem.ids.trakt, watched) // bypass
    }

    fun deleteItem(movieId: Int) {
        prefsRepository.deleteItemOnDB(movieId) // bypass
    }
}