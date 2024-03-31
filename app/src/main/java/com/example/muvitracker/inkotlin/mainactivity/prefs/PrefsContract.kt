package com.example.muvitracker.inkotlin.mainactivity.prefs

import com.example.muvitracker.inkotlin.repo.dto.DetaDto

interface PrefsContract {

    interface View {

        // GET
        fun updateUi(list :List<DetaDto>)

        // SET
        // liked watched - callbacks


        // altro
        fun startDetailsFragment(movieId: Int)




    }


    interface Presenter{


        // GET
        fun getPrefsListAndUpdateUi()


        // SET
        fun toggleFovoriteItem(dtoToTogggle: DetaDto)

        fun updateWatchedItem (updatedDto: DetaDto)


        // altro
        fun onVHolderClick (movieId: Int)

    }


}