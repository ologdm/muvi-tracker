package com.example.muvitracker.inkotlin.ui.mainactivity.prefs

import com.example.muvitracker.inkotlin.data.dto.DetaDto

interface PrefsContract {

    interface View {

        // get
        fun updateUi(list :List<DetaDto>)

        // altro
        fun startDetailsFragment(movieId: Int)
    }


    interface Presenter{

        // load
        fun loadPrefsListAndUpdateUi()

        // set
        fun toggleFovoriteItem(dtoToTogggle: DetaDto)
        fun updateWatchedItem (updatedDto: DetaDto)

        // altro
        fun onVHolderClick (movieId: Int)

    }


}