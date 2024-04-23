package com.example.muvitracker.inkotlin.mainactivity.details

import com.example.muvitracker.inkotlin.model.dto.DetaDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum


interface DetaContract {

    interface View {

        // 1°GET
        fun updateUi(detaDto: DetaDto) //aggiorna ui con dto presenter

        fun handleEmptyStates(emptyStates: EmptyStatesEnum)


        // private
        //fun updateFavoriteIcon(isFavorite: Boolean) // aggiorna con input da repo
        //fun updateWatchedCheckbox(isWatched: Boolean) // aggiorna con input da repo

    }


    interface Presenter {

        // GET
        fun getMovie(movieId: Int, forceRefresh: Boolean)


        // SET
        fun updateWatched(watchedStatus: Boolean)

        fun toggleFavorite()

    }


}