package com.example.muvitracker.ui.main.prefs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.data.prefs.PrefsRepo
import com.example.muvitracker.data.dto.DetailDto

class PrefsViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repository = PrefsRepo.getInstance(application)
    val preftList = MutableLiveData<List<DetailDto>>()


    fun updatePrefList() {
        preftList.value = repository.filterPrefsFromDetails()
    }

    fun toggleFovoriteItem(dtoToToggle: DetailDto) {
        repository.toggleFavoriteOnDB(dtoToToggle)
    }

    fun updateWatchedItem(updatedDto: DetailDto) {
        repository.updateWatchedOnDB(updatedDto)
    }


}