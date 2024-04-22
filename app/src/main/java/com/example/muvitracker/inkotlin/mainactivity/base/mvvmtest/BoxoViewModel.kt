package com.example.muvitracker.inkotlin.mainactivity.base.mvvmtest

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muvitracker.inkotlin.mainactivity.base.MovieModel
import com.example.muvitracker.inkotlin.model.boxo.BoxoRepo
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallbackList
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum

class BoxoViewModel(private val context: Context) : ViewModel() {

    private val repository = BoxoRepo.getInstance(context)

    // 2 stati da controllare
    val boxoList = MutableLiveData<List<MovieModel>>()
    val emptyState = MutableLiveData<EmptyStatesEnum>()


    fun getMovies(isRefresh: Boolean) {

        repository.getMovieList(object : EmptyStatesCallbackList<MovieModel> {
            override fun OnStart() {
                if (isRefresh) {
                    emptyState.value=EmptyStatesEnum.ON_FORCE_REFRESH
                } else {
                    emptyState.value=EmptyStatesEnum.ON_START
                }
            }

            override fun onSuccess(list: List<MovieModel>) {
                boxoList.value = list
                emptyState.value=EmptyStatesEnum.ON_SUCCESS
            }

            override fun onErrorIO() {
                emptyState.value=EmptyStatesEnum.ON_ERROR_IO
            }

            override fun onErrorOther() {
                emptyState.value=EmptyStatesEnum.ON_ERROR_OTHER
            }

        })
    }


}