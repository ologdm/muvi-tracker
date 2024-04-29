package com.example.muvitracker.inkotlin.ui.mainactivity.boxo

import android.content.Context
import com.example.muvitracker.inkotlin.domain.MovieModel
import com.example.muvitracker.inkotlin.data.boxo.BoxoRepo
import com.example.muvitracker.inkotlin.ui.mainactivity.base.MovieContract
import com.example.muvitracker.inkotlin.utils.EmptyStatesCallbackList
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum

class BoxoPresenter(
    private val view: MovieContract.View,
    private val context: Context
) : MovieContract.Presenter {

    val boxoRepo = BoxoRepo.getInstance(context)


    // CONTRACT METHODS
    override fun loadMovieAndUpdateUi(forceRefresh: Boolean) {

        boxoRepo.getMovieList(object : EmptyStatesCallbackList<MovieModel> {

            override fun OnStart() {
                if (forceRefresh) {
                    view.handleEmptyStates(EmptyStatesEnum.ON_FORCE_REFRESH)
                    println("XXX_BOXO_FRAGM_FORCE REFRESH")
                } else {
                    view.handleEmptyStates(EmptyStatesEnum.ON_START)
                    println("XXX_BOXO_FRAGM_START")
                }
            }

            override fun onSuccess(list: List<MovieModel>) {
                view.updateUi(list) // server list
                view.handleEmptyStates(EmptyStatesEnum.ON_SUCCESS)
                println("XXX_BOXO_FRAGM_SUCCESS")
            }

            override fun onErrorIO() {
                view.handleEmptyStates(EmptyStatesEnum.ON_ERROR_IO)
                println("XXX_BOXO_FRAGM_ERROR IO")
            }

            override fun onErrorOther() {
                view.handleEmptyStates(EmptyStatesEnum.ON_ERROR_OTHER)
                println("XXX_BOXO_FRAGM_ERROR OTHER")
            }
        })
    }


    override fun onVHolderClick(movieId: Int) {
        view.startDetailsFragment(movieId)
    }


}


