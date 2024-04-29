package com.example.muvitracker.inkotlin.ui.mainactivity.boxo.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.inkotlin.domain.MovieModel
import com.example.muvitracker.inkotlin.data.boxo.BoxoRepo
import com.example.muvitracker.inkotlin.utils.EmptyStatesCallbackList
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum

class BoxoViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repository = BoxoRepo.getInstance(application)

    // 2 stati da controllare
    val boxoList = MutableLiveData<List<MovieModel>>()
    val emptyState = MutableLiveData<EmptyStatesEnum>()


    fun loadMovies(isRefresh: Boolean) {

        repository.getMovieList(object : EmptyStatesCallbackList<MovieModel> {
            override fun OnStart() {
                if (isRefresh) {
                    emptyState.value= EmptyStatesEnum.ON_FORCE_REFRESH
                } else {
                    emptyState.value= EmptyStatesEnum.ON_START
                }
            }

            override fun onSuccess(list: List<MovieModel>) {
                boxoList.value = list
                emptyState.value= EmptyStatesEnum.ON_SUCCESS
            }

            override fun onErrorIO() {
                emptyState.value= EmptyStatesEnum.ON_ERROR_IO
            }

            override fun onErrorOther() {
                emptyState.value= EmptyStatesEnum.ON_ERROR_OTHER
            }

        })
    }


}