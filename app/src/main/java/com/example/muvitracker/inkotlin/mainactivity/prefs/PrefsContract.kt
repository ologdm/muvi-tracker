package com.example.muvitracker.inkotlin.mainactivity.prefs

import com.example.muvitracker.inkotlin.repo.dto.DetaMovDto

interface PrefsContract {

    interface View {

        // GET
        fun updateUi(list :List<DetaMovDto>)

        // SET
        // liked watched - callbacks


        // altro
        fun startDetailsFragment(movieId: Int)




    }


    interface Presenter{


        // GET
        fun getPrefsListAndUpdateUi()


        // SET
        fun toggleFovoriteItem(dtoToTogggle: DetaMovDto)

        fun updateWatchedItem (updatedDto: DetaMovDto)


        // altro
        fun onVHolderClick (movieId: Int)

    }


}