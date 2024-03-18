package com.example.muvitracker.mainactivity.kotlin.details

import com.example.muvitracker.repo.kotlin.dto.DetaDto


interface DetaContract {

    interface View {


        fun updateUi(detaDto: DetaDto) //aggiorna ui con dto presenter

        // parte finale
        fun updateFavoriteIcon(isFavorite: Boolean) // aggiorna con input da repo
        fun updateWatchedCheckbox(isWatched: Boolean) // aggiorna con input da repo

    }


    interface Presenter {


        fun loadData(detaDto: DetaDto)
        // check se elemento nella lista
        // TODO: from server
        // TODO: from mylist


        //parte finale
        fun updateWatched(watchedStatus: Boolean) // aggiorna repo, aggiorna presenter dto da repo
        fun toggleFavorite(watchedStatus: Boolean) // input toggle stato a repo, e aggiorna view da repo


    }


}