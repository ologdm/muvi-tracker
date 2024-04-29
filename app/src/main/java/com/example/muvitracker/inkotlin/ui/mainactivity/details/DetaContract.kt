package com.example.muvitracker.inkotlin.ui.mainactivity.details

import com.example.muvitracker.inkotlin.data.dto.DetaDto
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum


interface DetaContract {

    interface View {

        // 1°LOAD
        fun updateUi(detaDto: DetaDto) //aggiorna ui con dto presenter

        fun handleEmptyStates(emptyStates: EmptyStatesEnum)

        // private
        //fun updateFavoriteIcon(isFavorite: Boolean) // aggiorna con input da repo
        //fun updateWatchedCheckbox(isWatched: Boolean) // aggiorna con input da repo
    }


    interface Presenter {

        // LOAD
        fun loadMovie(movieId: Int, forceRefresh: Boolean)

        // SET
        fun updateWatched(watchedStatus: Boolean)
        fun toggleFavorite()

    }


}