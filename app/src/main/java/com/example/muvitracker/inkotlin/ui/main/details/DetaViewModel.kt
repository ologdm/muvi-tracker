package com.example.muvitracker.inkotlin.ui.main.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.inkotlin.data.detail.DetailRepository
import com.example.muvitracker.inkotlin.data.dto.DetailDto
import com.example.muvitracker.inkotlin.utils.StateContainer

// context == application
// context viene implementato da application, activity e service

class DetaViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repository = DetailRepository.getInstance(application)
    val stateContainer = MutableLiveData<StateContainer<DetailDto>>()


    fun loadDetail() {

    }


    fun toggleFavorite() {
        repository.toggleFavoriteOnDB(viewModelDto.value!!)     // set dto attuale a repo
        viewModelDto.value =
            repository.getLocalItem(viewModelDto.value!!.ids.trakt)    // get quello aggiornato
    }


    fun updateWatched(watchedStatus: Boolean) {
        val modifiedDto = viewModelDto.value?.copy(watched = watchedStatus)    //cambio stato + copy

        if (modifiedDto != null) {
            repository.updateWatchedOnDB(modifiedDto)    // send modified dto to repo
            viewModelDto.value = modifiedDto     // get quello aggiornato
        }
    }


}