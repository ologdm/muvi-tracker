package com.example.muvitracker.inkotlin.mainactivity.deta

import com.example.muvitracker.inkotlin.repo.dto.DetaDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnumNew


interface DetaContract {

    interface View {

        // 1°GET
        fun updateUi(detaDto: DetaDto) //aggiorna ui con dto presenter

        fun emptyStatesFlow(emptyStates: EmptyStatesEnumNew)


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