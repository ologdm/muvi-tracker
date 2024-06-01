package com.example.muvitracker.inkotlin.ui.main.details.old

import android.content.Context
import com.example.muvitracker.inkotlin.data.detail.OldDetaRepo
import com.example.muvitracker.inkotlin.data.dto.DetailDto
import com.example.muvitracker.inkotlin.utils.EmptyStatesCallback
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum

/**
 *  LOAD
 *  -loadMovie(): OK
 *      > loadMovie da repo
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
 */


class DetaPresenter(
    private val view: DetaContract.View,
    private val context: Context
) : DetaContract.Presenter {

    private val repository = OldDetaRepo.getInstance(context)
    private var presenterDto: DetailDto? = null


    //CONTRACT METHODS

    // 1. LOAD OK
    override fun loadMovie(movieId: Int, forceRefresh: Boolean) {
        // copia da repo
        repository.getMovie(
            movieId,
            wrapperESCallback(forceRefresh) // aggiorno dto da call
        )
    }

    // solo per getMovie OK
    private fun wrapperESCallback(forceRefresh: Boolean): EmptyStatesCallback<DetailDto> {

        return object : EmptyStatesCallback<DetailDto> {
            override fun OnStart() {
                if (forceRefresh) {
                    view.handleEmptyStates(EmptyStatesEnum.ON_FORCE_REFRESH)
                    println("XXX_PRES_EMPTY STATE FORCE REFRESH")
                } else {
                    view.handleEmptyStates(EmptyStatesEnum.ON_START)
                    println("XXX_PRES_EMPTY STATE START")
                }
            }

            override fun onSuccess(obj: DetailDto) {
                view.handleEmptyStates(EmptyStatesEnum.ON_SUCCESS)
                presenterDto = obj.copy() // update presenter and ui
                updateUi()
                println("XXX_PRES_EMPTY STATE SUCCESS")
            }

            override fun onErrorIO() {
                view.handleEmptyStates(EmptyStatesEnum.ON_ERROR_IO)
                println("XXX_PRES_ES_ERROR IO")
            }

            override fun onErrorOther() {
                view.handleEmptyStates(EmptyStatesEnum.ON_ERROR_OTHER)
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




