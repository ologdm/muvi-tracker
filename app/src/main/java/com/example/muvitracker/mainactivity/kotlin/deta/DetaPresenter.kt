package com.example.muvitracker.mainactivity.kotlin.deta

import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.EmptyStatesCallback
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum


/**
 *  GET
 *  getMovie():
 *      1) aggiorna presenterDto,
 *      2) gestisce empty states, con stati callback
 *      chiamata server(onStart, onSuccess, onErrorIO, onErrorOther
 *
 * SET
 *
 *
 */


class DetaPresenter(
    private val view: DetaContract.View
) : DetaContract.Presenter {


    // ATTRIBUTI
    // dto
    private lateinit var presenterDto: DetaDto // elemento usato per mostrare elementi su Fragment

    // repo
    private val detaRepo = DetaRepo //


    //CONTRACT METHODS

    // 1. GET OK
    override fun getMovie(movieId: Int, forceRefresh: Boolean) {

        // copia da repo
        presenterDto = detaRepo
            .getMovie(
                movieId,
                object : EmptyStatesCallback { // solo per la fase server
                    override fun OnStart() {
                        if (forceRefresh)
                            view.emptyStatesFlow(EmptyStatesEnum.ON_FORCE_REFRESH)
                        else
                            view.emptyStatesFlow(EmptyStatesEnum.ON_START)
                    }

                    override fun onSuccess() {
                        view.emptyStatesFlow(EmptyStatesEnum.ON_SUCCESS)
                    }

                    override fun onErrorIO() {
                        view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_IO)
                    }

                    override fun onErrorOther() {
                        view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_OTHER)
                    }
                }
            )
            .copy() // >> dataclass ha copy() incorporato


        // aggiorna da presenter
        view.updateUi(presenterDto)

    }


    // 2. SET

    // chiama al click su Icon
    override fun toggleFavorite() {
        // delego il lavoro logico a repo
        detaRepo.toggleFavoriteOnDB(presenterDto)

        // aggiorno da db
        updateDtoPresenterAndUi()

    }


    override fun updateWatched(watchedStatus: Boolean) {
        //cambio stato
        presenterDto.watched = watchedStatus

        // invio elemento cambiato
        detaRepo.updateWatchedOnDB(presenterDto)

        // aggiorno da db
        updateDtoPresenterAndUi()
    }


    // interno
    private fun updateDtoPresenterAndUi() {
        // salva copia elemento
        presenterDto = detaRepo
            .getLocalItem(presenterDto.ids.trakt)
            .copy()

        // aggionaUi con dati nuovi
        view.updateUi(presenterDto)
    }


}