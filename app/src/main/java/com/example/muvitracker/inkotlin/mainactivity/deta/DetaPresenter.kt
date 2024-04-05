package com.example.muvitracker.inkotlin.mainactivity.deta

import android.content.Context
import com.example.muvitracker.inkotlin.mainactivity.deta.repo.DetaRepo
import com.example.muvitracker.inkotlin.repo.dto.DetaDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallback
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnumNew


/**
 *
 * fatto tutti alle 16:34 20 mar
 *
 *  GET
 *  -getMovie(): OK
 *      > getMovie da repo
 *      - wrapperESCallback() - solo per getMovie OK
 *          > stati (onStart, onSuccess, onErrorIO, onErrorOther)
 *          > gestisce empty states
 *          > (onSuccess) - aggiorna presenter e Ui
 *
 *
 * SET
 * -toggleFavorite() OK
 *          > send to repo (the dto to modify),
 *          > get from repo
 *          > updateUi
 *
 * -updateWatched() OK
 *          > modify dto state and create copy
 *          > send copy to repo
 *          > update presenterDto with copy
 *          > updateUi
 *
 *
 */


// copy: val-val, solo quando modifico
// !! con if() vabene


class DetaPresenter(
    private val view: DetaContract.View,
    private val context: Context
) : DetaContract.Presenter {


    private val detaRepo = DetaRepo.getInstance(context)
    private var presenterDto: DetaDto? = null


    //CONTRACT METHODS

    // 1. GET OK
    override fun getMovie(movieId: Int, forceRefresh: Boolean) {
        // copia da repo
        detaRepo.getMovie(
            movieId,
            wrapperESCallback(forceRefresh) // aggiorno dto da call
        )
    }


    // solo per getMovie OK
    private fun wrapperESCallback(forceRefresh: Boolean): EmptyStatesCallback<DetaDto> {

        return object : EmptyStatesCallback<DetaDto> {

            override fun OnStart() {
                if (forceRefresh) {
                    view.emptyStatesFlow(EmptyStatesEnumNew.ON_FORCE_REFRESH)
                    println("XXX_PRES_EMPTY STATE FORCE REFRESH")
                } else {
                    view.emptyStatesFlow(EmptyStatesEnumNew.ON_START)
                    println("XXX_PRES_EMPTY STATE START")
                }
            }

            override fun onSuccess(obj: DetaDto) {
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_SUCCESS)
                presenterDto = obj.copy() // update presenter and ui
                updateUi()
                println("XXX_PRES_EMPTY STATE SUCCESS")
            }

            override fun onErrorIO() {
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_ERROR_IO)
                println("XXX_PRES_ES_ERROR IO")
            }

            override fun onErrorOther() {
                view.emptyStatesFlow(EmptyStatesEnumNew.ON_ERROR_OTHER)
                println("XXX_PRES_ES_ERROR OTHER")
            }
        }
    }


    // OK
    private fun updateUi() {
        if (presenterDto != null) {
            view.updateUi(presenterDto!!)
        }
    }


    // 2. SET
    // OK
    override fun toggleFavorite() {
        if (presenterDto != null) {
            // send to repo
            detaRepo.toggleFavoriteOnDB(presenterDto!!) // delego il lavoro logico a repo
            // get from repo
            presenterDto = detaRepo.getLocalItem(presenterDto!!.ids.trakt)
        }
        updateUi()
        println("XXX_PRES_TOGGLE")
    }


    // OK
    override fun updateWatched(watchedStatus: Boolean) {
        //cambio stato + copy
        val modifiedDto = presenterDto?.copy(watched = watchedStatus)

        // OK
        if (modifiedDto != null) {
            // send to repo
            detaRepo.updateWatchedOnDB(modifiedDto)
            // update presenterDto
            presenterDto = modifiedDto
        }
        updateUi()
        println("XXX_PRES_WATCHED")
    }

}



