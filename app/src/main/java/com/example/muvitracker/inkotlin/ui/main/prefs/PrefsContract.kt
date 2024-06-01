package com.example.muvitracker.inkotlin.ui.main.prefs

import com.example.muvitracker.inkotlin.data.dto.DetailDto

interface PrefsContract {

    interface View {

        // get
        fun updateUi(list :List<DetailDto>)

        // altro
        fun startDetailsFragment(movieId: Int)
    }


    interface Presenter{

        // load
        fun loadPrefsListAndUpdateUi()

        // set
        fun toggleFovoriteItem(dtoToTogggle: DetailDto)
        fun updateWatchedItem (updatedDto: DetailDto)

        // altro
        fun onVHolderClick (movieId: Int)

    }


}