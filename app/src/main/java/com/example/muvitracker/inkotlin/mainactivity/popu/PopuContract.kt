package com.example.muvitracker.inkotlin.mainactivity.popu

import com.example.muvitracker.inkotlin.repo.dto.PopuDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnumNew

interface PopuContract {


    interface View {

        fun updateUi(list: List<PopuDto>)

        fun startDetailsFragment(movieId: Int)

        //fun emptyStatesFlow(emptyStates: EmptyStatesEnum)
        fun emptyStatesFlow(emptyStates: EmptyStatesEnumNew)


    }


    interface Presenter {

        fun getMovieAndUpdateUi(forceRefresh: Boolean)

        fun onVHolderCLick(movieId: Int)
    }

}