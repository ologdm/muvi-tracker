package com.example.muvitracker.inkotlin.mainactivity.prefs.mvvm_test

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muvitracker.inkotlin.model.prefs.PrefsRepo
import com.example.muvitracker.inkotlin.model.dto.DetaDto

class PrefsViewModel(private val context: Context) : ViewModel() {

    private val repository = PrefsRepo.getInstance(context)

    val preftList = MutableLiveData<List<DetaDto>>()


    // get elements
    fun getPrefsList() {
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