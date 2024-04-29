package com.example.muvitracker.inkotlin.ui.mainactivity.popu

import android.content.Context
import com.example.muvitracker.inkotlin.domain.MovieModel
import com.example.muvitracker.inkotlin.data.popu.PopuRepo
import com.example.muvitracker.inkotlin.ui.mainactivity.base.MovieContract
import com.example.muvitracker.inkotlin.utils.EmptyStatesCallbackList
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum


/**
 *
 * METODI:
 *  loadMovieAndUpdateUi
 *              > ES stati
 *              > on success
 *
 *  onVHolderCLick
 *
 */

class PopuPresenter(
    private val view: MovieContract.View,
    private val context: Context
) : MovieContract.Presenter {

    private val popuRepo: PopuRepo = PopuRepo.getInstance(context)


    // CONTRACT METHODS
    // 2.1 OK
    override fun loadMovieAndUpdateUi(forceRefresh: Boolean) {

        popuRepo.getMovieList(object : EmptyStatesCallbackList<MovieModel> {
            override fun OnStart() {
                if (forceRefresh) {
                    view.handleEmptyStates(EmptyStatesEnum.ON_FORCE_REFRESH)
                    println("XXX_POPU_FRAGM_FORCE REFRESH")
                } else {
                    view.handleEmptyStates(EmptyStatesEnum.ON_START)
                    println("XXX_POPU_FRAGM_START")
                }
            }

            override fun onSuccess(list: List<MovieModel>) {
                view.updateUi(list) // server list
                view.handleEmptyStates(EmptyStatesEnum.ON_SUCCESS)
                println("XXX_POPU_FRAGM_SUCCESS")
            }

            override fun onErrorIO() {
                view.handleEmptyStates(EmptyStatesEnum.ON_ERROR_IO)
                println("XXX_POPU_FRAGM_ERROR IO")
            }

            override fun onErrorOther() {
                view.handleEmptyStates(EmptyStatesEnum.ON_ERROR_OTHER)
                println("XXX_POPU_FRAGM_ERROR OTHER")
            }
        })
    }


    // 2.2 OK
    override fun onVHolderClick(movieId: Int) {
        view.startDetailsFragment(movieId)
    }

}