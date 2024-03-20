package com.example.muvitracker.mainactivity.kotlin.prefs

import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum

interface PrefsContract {

    interface View {

        fun undateUi(list :List<DetaDto>)

        fun startDetailsFragment(movieId: Int)

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum)



    }


    interface Presenter{

        fun onVHolderClick (movieId: Int)

    }


}