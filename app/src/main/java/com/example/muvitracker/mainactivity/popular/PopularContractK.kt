package com.example.muvitracker.mainactivity.popular

import com.example.muvitracker.repository.dto.PopularDtoK
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum

interface PopularContractK {

    // OK
    interface View {


        // OK
        fun UpdateUi(list: List<PopularDtoK>)


        // OK
        fun startDetailsFragment(movieId: Int)

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum)


    }

    // OK
    interface Presenter {


        fun serverCallAndUpdate(forceRefresh: Boolean)

        fun onVHolderCLick(movieId: Int)
    }

}