package com.example.muvitracker.mainactivity.kotlin.deta

import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum


interface DetaContract {

    interface View {

        // 1°GET
        fun updateUi(detaDto: DetaDto) //aggiorna ui con dto presenter

        // private
        //fun updateFavoriteIcon(isFavorite: Boolean) // aggiorna con input da repo
        //fun updateWatchedCheckbox(isWatched: Boolean) // aggiorna con input da repo

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum)

    }


    interface Presenter {

        // 1° GET
        fun getMovie(movieId: Int, forceRefresh: Boolean)
        // aggiorna dto(copia) OK
        // aggiorna ui da dto OK


        //parte finale SET
        fun updateWatched(watchedStatus: Boolean)
        // invia  stato a repo(repo gestisce),
        // chiedi a repo e aggiorna con copia dto e ui


        fun toggleFavorite()
        // input toggle stato a repo,
        // chiedi a repo e aggiorna con copia dto e ui


    }


}