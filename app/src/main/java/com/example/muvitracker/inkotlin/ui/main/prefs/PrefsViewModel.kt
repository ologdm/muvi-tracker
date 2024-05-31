package com.example.muvitracker.inkotlin.ui.main.prefs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.inkotlin.data.prefs.PrefsRepo
import com.example.muvitracker.inkotlin.data.dto.DetaDto

class PrefsViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repository = PrefsRepo.getInstance(application)

    val preftList = MutableLiveData<List<DetaDto>>()


    // load elements
    fun updatePrefList() {
        preftList.value = repository.filterPrefsFromDetails()
    }


    // toggle liked, set watched
    fun toggleFovoriteItem(dtoToToggle: DetaDto) {
        repository.toggleFavoriteOnDB(dtoToToggle)
    }

    // passare e modificare
    fun updateWatchedItem(updatedDto: DetaDto) {
        repository.updateWatchedOnDB(updatedDto)
    }


}