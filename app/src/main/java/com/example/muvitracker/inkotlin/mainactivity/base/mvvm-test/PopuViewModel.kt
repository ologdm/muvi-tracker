package com.example.muvitracker.inkotlin.mainactivity.base.`mvvm-test`

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muvitracker.inkotlin.model.MovieModel
import com.example.muvitracker.inkotlin.model.popu.PopuRepo
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallbackList
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum

class PopuViewModel(
    private val context: Context
) : ViewModel() {

    val repository = PopuRepo.getInstance(context)


    // DataLive - observable OK
    // 1. movieList - parametro UpdateUi MVP OK
    val popuList = MutableLiveData<List<MovieModel>>()

    // 2. stato empty states - Parametro HandleES MVP OK
    val emptyState = MutableLiveData<EmptyStatesEnum>()


    fun getMovie(isRefresh: Boolean) {
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