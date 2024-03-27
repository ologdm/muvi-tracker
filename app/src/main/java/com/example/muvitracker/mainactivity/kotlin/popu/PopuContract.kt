package com.example.muvitracker.mainactivity.kotlin.popu

import com.example.muvitracker.repo.kotlin.dto.PopuDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.EmptyStatesEnumNew

interface PopuContract {


    interface View {

        fun UpdateUi(list: List<PopuDto>)

        fun startDetailsFragment(movieId: Int)

        //fun emptyStatesFlow(emptyStates: EmptyStatesEnum)
        fun emptyStatesFlow(emptyStates: EmptyStatesEnumNew)


    }


    interface Presenter {

        fun getMovieAndUpdateUi(forceRefresh: Boolean)

        fun onVHolderCLick(movieId: Int)
    }

}