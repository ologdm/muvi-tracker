package com.example.muvitracker.ui.main.prefs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.muvitracker.data.prefs.PrefsRepository
import com.example.muvitracker.domain.model.DetailMovie

class PrefsViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val prefsRepository = PrefsRepository.getInstance(application)


    val preftList = prefsRepository.getList()

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