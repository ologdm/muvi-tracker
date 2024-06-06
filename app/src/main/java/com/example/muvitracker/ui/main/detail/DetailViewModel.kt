package com.example.muvitracker.ui.main.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.data.detail.DetailRepository
import com.example.muvitracker.data.prefs.PrefsRepository
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer


class DetailViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val detailRepository = DetailRepository.getInstance(application)
    private val prefsRepository = PrefsRepository.getInstance(application)


    val stateContainer = MutableLiveData<StateContainer<DetailMovie>>()


    // load from detailRepository, gia DetailMovie (deve contenere info e stati)
    // aggiorno viewmodel con dato nuovo
    fun loadDetail(movieId: Int) {

//       ???? detailRepository.getMovieFromLocal(movieId) // fun diretta Local : DetailMovie

        detailRepository.getDetailMovie( // cerca su local poi scarica da internet
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