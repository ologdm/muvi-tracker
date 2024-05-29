package com.example.muvitracker.inkotlin.ui.main.details.mvvm_test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.inkotlin.data.details.DetaRepo
import com.example.muvitracker.inkotlin.data.dto.DetaDto
import com.example.muvitracker.inkotlin.utils.EmptyStatesCallback
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum

// context == aplpication
// context - viene impleemtnato da application, activity e service

class DetaViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repository = DetaRepo.getInstance(application)

    // Observable OK
    val viewModelDto = MutableLiveData<DetaDto>()
    val emptyState = MutableLiveData<EmptyStatesEnum>()


    // GET MOVIE OK
    fun getMovie(movieId: Int, forceRefresh: Boolean) {
        // copia da repo
        repository.getMovie(movieId, wrapperESCallback(forceRefresh)) // aggiorno dto da call
    }

    private fun wrapperESCallback(forceRefresh: Boolean): EmptyStatesCallback<DetaDto> {

        return object : EmptyStatesCallback<DetaDto> {

            override fun OnStart() {
                if (forceRefresh) {
                    //view.emptyStatesFlow(EmptyStatesEnum.ON_FORCE_REFRESH)
                    emptyState.value = EmptyStatesEnum.ON_FORCE_REFRESH

                } else {
                    //view.emptyStatesFlow(EmptyStatesEnum.ON_START)
                    emptyState.value = EmptyStatesEnum.ON_START

                }
            }

            override fun onSuccess(obj: DetaDto) {
                //view.emptyStatesFlow(EmptyStatesEnum.ON_SUCCESS)
                emptyState.value = EmptyStatesEnum.ON_SUCCESS
                //updateUi()
                viewModelDto.value = obj


            }

            override fun onErrorIO() {
                //view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_IO)
                emptyState.value = EmptyStatesEnum.ON_ERROR_IO

            }

            override fun onErrorOther() {
                //view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_OTHER)
                emptyState.value = EmptyStatesEnum.ON_ERROR_OTHER

            }
        }
    }


    fun toggleFavorite() {
        // set dto attuale a repo
        repository.toggleFavoriteOnDB(viewModelDto.value!!)
        // get quello aggiornato
        viewModelDto.value = repository.getLocalItem(viewModelDto.value!!.ids.trakt)
    }

    fun updateWatched(watchedStatus: Boolean) {
        //cambio stato + copy
        val modifiedDto = viewModelDto.value?.copy(watched = watchedStatus)

        // OK
        if (modifiedDto != null) {
            // send modified dto to repo
            repository.updateWatchedOnDB(modifiedDto)
            // get quello aggiornato
            viewModelDto.value = modifiedDto
        }
        println("XXX_PRES_WATCHED")
    }


}