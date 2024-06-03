package com.example.muvitracker.ui.main.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.data.detail.DetailRepository
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer

// context == application
// context viene implementato da application, activity e service

class DetaViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repository = DetailRepository.getInstance(application)

    val stateContainer = MutableLiveData<StateContainer<DetailDto>>()


    // TODO new ES
    fun loadDetail(movieId: Int, forceRefresh: Boolean) {

        repository.getMovieFromLocal(movieId) // carica subito

        repository.getMovie(
            movieId,
            onResponse = { repoRespopnse ->
                when (repoRespopnse) {
                    is IoResponse.Success -> {
                        stateContainer.value = StateContainer(data = repoRespopnse.dataValue)
                    }

                    is IoResponse.NetworkError -> {
                        stateContainer.value = StateContainer(isNetworkError = true)
                    }

                    is IoResponse.OtherError -> {
                        stateContainer.value = StateContainer(isOtherError = true)
                    }

                }
            })
    }


    fun toggleFavorite() {
        repository.toggleFavoriteOnDB(stateContainer.value?.data!!)     // set dto attuale a repo
        stateContainer.value?.data =
            repository.getMovieFromLocal(stateContainer.value?.data?.ids!!.trakt)    // get quello aggiornato
    }


    fun updateWatched(watchedStatus: Boolean) {
        val modifiedDto =
            stateContainer.value?.data?.copy(watched = watchedStatus)    //cambio stato + copy

        if (modifiedDto != null) {
            repository.updateWatchedOnDB(modifiedDto)    // send modified dto to repo
            stateContainer.value?.data = modifiedDto     // get quello aggiornato
        }
    }


}