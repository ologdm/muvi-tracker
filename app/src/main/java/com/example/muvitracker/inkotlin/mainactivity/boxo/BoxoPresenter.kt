package com.example.muvitracker.inkotlin.mainactivity.boxo

import android.content.Context
import com.example.muvitracker.inkotlin.mainactivity.boxo.repo.BoxoRepo
import com.example.muvitracker.inkotlin.repo.dto.BoxoDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallbackList
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnumNew

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
                    view.emptyStatesFlow(EmptyStatesEnumNew.ON_FORCE_REFRESH)
                    println("XXX_BOXO_FRAGM_FORCE REFRESH")
                } else {
                    view.emptyStatesFlow(EmptyStatesEnumNew.ON_START)
                    println("XXX_BOXO_FRAGM_START")
                }
            }

            override fun onSuccess(list: List<BoxoDto>) {
                view.updateUi(list) // server list
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_SUCCESS)
                println("XXX_BOXO_FRAGM_SUCCESS")
            }

            override fun onErrorIO() {
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_ERROR_IO)
                println("XXX_BOXO_FRAGM_ERROR IO")
            }

            override fun onErrorOther() {
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_ERROR_OTHER)
                println("XXX_BOXO_FRAGM_ERROR OTHER")
            }
        })
    }


    // OK
    override fun onVHolderClick(traktMovieId: Int) {
        view.startDetailsFragment(traktMovieId)
    }


}


