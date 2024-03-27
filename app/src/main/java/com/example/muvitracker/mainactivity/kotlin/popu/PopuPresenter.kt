package com.example.muvitracker.mainactivity.kotlin.popu

import android.content.Context
import com.example.muvitracker.mainactivity.kotlin.popu.repo.PopuRepo
import com.example.muvitracker.repo.kotlin.dto.PopuDto
import com.example.muvitracker.utils.kotlin.EmptyStatesCallbackList
import com.example.muvitracker.utils.kotlin.EmptyStatesEnumNew


/**
 *
 * METODI:
 * getMovieAndUpdateUi
 *              > ES stati
 *              > on success
 *

 *
 * onVHolderCLick
 *
 */

class PopuPresenter(
    private val view: PopuContract.View, // view nel costruttore, posso inserire chi implementa view
    private val context: Context

) : PopuContract.Presenter {


    // ATTRIBUTI OK
    // context - nel costruttore OK
    private val popuRepo: PopuRepo = PopuRepo.getInstance(context)


    // CONTRACT METHODS
    // 2.1 OK
    override fun getMovieAndUpdateUi(forceRefresh: Boolean) {


        popuRepo.getMovieList(object : EmptyStatesCallbackList<PopuDto> {
            override fun OnStart() {
                if (forceRefresh) {
                    view.emptyStatesFlow(EmptyStatesEnumNew.ON_FORCE_REFRESH)
                    println("XXX_POPU_FRAGM_FORCE REFRESH")
                } else {
                    view.emptyStatesFlow(EmptyStatesEnumNew.ON_START)
                    println("XXX_POPU_FRAGM_START")
                }
            }

            override fun onSuccess(list: List<PopuDto>) {

                view.updateUi(list) // server list
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_SUCCESS)
                println("XXX_POPU_FRAGM_SUCCESS")
            }

            override fun onErrorIO() {
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_ERROR_IO)
                println("XXX_POPU_FRAGM_ERROR IO")
            }

            override fun onErrorOther() {
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_ERROR_OTHER)
                println("XXX_POPU_FRAGM_ERROR OTHER")
            }


        })

    }


    // 2.2 OK
    override fun onVHolderCLick(movieId: Int) {
        view.startDetailsFragment(movieId)
    }
}