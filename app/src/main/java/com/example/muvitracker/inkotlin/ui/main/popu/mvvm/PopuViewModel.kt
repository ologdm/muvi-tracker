package com.example.muvitracker.inkotlin.ui.main.popu.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.inkotlin.domain.MovieModel
import com.example.muvitracker.inkotlin.data.popu.PopuRepo
import com.example.muvitracker.inkotlin.utils.EmptyStatesCallbackList
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum

class PopuViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    // AndroidViewModel - permette di passare un figlio di context


    val repository = PopuRepo.getInstance(application)


    // DataLive - observable OK
    // 1. movieList - parametro UpdateUi MVP OK
    val popuList = MutableLiveData<List<MovieModel>>()

    // 2. stato empty states - Parametro HandleES MVP OK
    val emptyState = MutableLiveData<EmptyStatesEnum>()


    fun loadMovies(isRefresh: Boolean) {
        repository.getMovieList(object : EmptyStatesCallbackList<MovieModel> {
            override fun OnStart() {
                if (isRefresh){
                    emptyState.value = EmptyStatesEnum.ON_FORCE_REFRESH
                } else{
                    emptyState.value = EmptyStatesEnum.ON_START
                }
            }

            override fun onSuccess(list: List<MovieModel>) {
                popuList.value = list // assegnazione stile LiveData
                // no aggiornamento
                emptyState.value = EmptyStatesEnum.ON_SUCCESS
            }

            override fun onErrorIO() {
                emptyState.value = EmptyStatesEnum.ON_ERROR_IO
            }

            override fun onErrorOther() {
                emptyState.value = EmptyStatesEnum.ON_ERROR_OTHER
            }

        })
    }


    // note LiveData:
    // 1. aggiorno valore lista, che viene osservato da view e passata poi all'adapter
    // 2. aggiorno valore stato emptyStates, che verrà osservato da view aggiornando
    //    il comportamento in base allo stato

    // manca il passaggio funzione intermedia su presenter che chiama le funzioni di contractView
    // ma le funzioni sono chiamate direttamente da view

}