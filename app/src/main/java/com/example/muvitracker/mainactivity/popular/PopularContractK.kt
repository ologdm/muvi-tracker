package com.example.muvitracker.mainactivity.popular

import com.example.muvitracker.repository.dto.PopularDtoK
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum

interface PopularContractK {


    interface View {

        fun UpdateUi(list: List<PopularDtoK>)

        fun startDetailsFragment(movieId: Int)

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum)


    }


    interface Presenter {

        fun serverCallAndUpdate(forceRefresh: Boolean)

        fun onVHolderCLick(movieId: Int)
    }

}