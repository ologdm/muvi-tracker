package com.example.muvitracker.inkotlin.mainactivity.details

import android.content.Context
import com.example.muvitracker.inkotlin.model.details.DetaRepo
import com.example.muvitracker.inkotlin.model.dto.DetaDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesCallback
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum


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


    private val repository = DetaRepo.getInstance(context)
    private var presenterDto: DetaDto? = null


    //CONTRACT METHODS

    // 1. GET OK
    override fun getMovie(movieId: Int, forceRefresh: Boolean) {
        // copia da repo
        repository.getMovie(
            movieId,
            wrapperESCallback(forceRefresh) // aggiorno dto da call
        )
    }


    // solo per getMovie OK
    private fun wrapperESCallback(forceRefresh: Boolean): EmptyStatesCallback<DetaDto> {

        return object : EmptyStatesCallback<DetaDto> {

            override fun OnStart() {
                if (forceRefresh) {
                    view.emptyStatesFlow(EmptyStatesEnum.ON_FORCE_REFRESH)
                    println("XXX_PRES_EMPTY STATE FORCE REFRESH")
                } else {
                    view.emptyStatesFlow(EmptyStatesEnum.ON_START)
                    println("XXX_PRES_EMPTY STATE START")
                }
            }

            override fun onSuccess(obj: DetaDto) {
                view.emptyStatesFlow(EmptyStatesEnum.ON_SUCCESS)
                presenterDto = obj.copy() // update presenter and ui
                updateUi()
                println("XXX_PRES_EMPTY STATE SUCCESS")
            }

            override fun onErrorIO() {
                view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_IO)
                println("XXX_PRES_ES_ERROR IO")
            }

            override fun onErrorOther() {
                view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_OTHER)
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
            repository.toggleFavoriteOnDB(presenterDto!!) // delego il lavoro logico a repo
            // get from repo
            presenterDto = repository.getLocalItem(presenterDto!!.ids.trakt)
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
            repository.updateWatchedOnDB(modifiedDto)
            // update presenterDto
            presenterDto = modifiedDto
        }
        updateUi()
        println("XXX_PRES_WATCHED")
    }

}




