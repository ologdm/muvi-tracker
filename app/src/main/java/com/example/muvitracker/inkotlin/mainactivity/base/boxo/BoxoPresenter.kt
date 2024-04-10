package com.example.muvitracker.inkotlin.mainactivity.base.boxo

import android.content.Context
import com.example.muvitracker.inkotlin.repo.boxo.BoxoRepo
import com.example.muvitracker.inkotlin.repo.dto.base.BoxoDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallbackList
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum

class BoxoPresenter(

    private val view: BoxoContract.View, // costruttore view
    private val context: Context

) : BoxoContract.Presenter {

    // ATTRIBUTO
    val boxoRepo = BoxoRepo.getInstance(context)


    // CONTRACT METHODS

    override fun getMovieAndUpdateUi(forceRefresh: Boolean) {

        boxoRepo.getMovieList(object : EmptyStatesCallbackList<BoxoDto> {

            override fun OnStart() {
                if (forceRefresh) {
                    view.emptyStatesFlow(EmptyStatesEnum.ON_FORCE_REFRESH)
                    println("XXX_BOXO_FRAGM_FORCE REFRESH")
                } else {
                    view.emptyStatesFlow(EmptyStatesEnum.ON_START)
                    println("XXX_BOXO_FRAGM_START")
                }
            }

            override fun onSuccess(list: List<BoxoDto>) {
                view.updateUi(list) // server list
                view.emptyStatesFlow(EmptyStatesEnum.ON_SUCCESS)
                println("XXX_BOXO_FRAGM_SUCCESS")
            }

            override fun onErrorIO() {
                view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_IO)
                println("XXX_BOXO_FRAGM_ERROR IO")
            }

            override fun onErrorOther() {
                view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_OTHER)
                println("XXX_BOXO_FRAGM_ERROR OTHER")
            }
        })
    }


    // OK
    override fun onVHolderClick(traktMovieId: Int) {
        view.startDetailsFragment(traktMovieId)
    }


}


