package com.example.muvitracker.mainactivity.kotlin.popu

import com.example.muvitracker.repo.kotlin.dto.PopuDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum

interface PopuContract {


    interface View {

        fun UpdateUi(list: List<PopuDto>)

        fun startDetailsFragment(movieId: Int)

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum)


    }


    interface Presenter {

        fun serverCallAndUpdate(forceRefresh: Boolean)

        fun onVHolderCLick(movieId: Int)
    }

}