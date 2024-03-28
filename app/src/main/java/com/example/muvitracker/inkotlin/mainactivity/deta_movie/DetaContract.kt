package com.example.muvitracker.inkotlin.mainactivity.deta_movie

import com.example.muvitracker.inkotlin.repo.dto.DetaMovDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum


interface DetaContract {

    interface View {

        // 1°GET
        fun updateUi(detaDto: DetaMovDto) //aggiorna ui con dto presenter

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum)


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