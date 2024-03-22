package com.example.muvitracker.mainactivity.kotlin.prefs

import com.example.muvitracker.repo.kotlin.dto.DetaDto

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
        fun getPrefsList()


        // SET
        fun toggleFovoriteItem(dtoToTogggle: DetaDto)

        fun updateWatchedItem (updatedDto: DetaDto)


        // altro
        fun onVHolderClick (movieId: Int)


    }


}