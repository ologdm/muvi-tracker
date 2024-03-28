package com.example.muvitracker.inkotlin.mainactivity.boxo

import com.example.muvitracker.inkotlin.repo.dto.BoxoDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnumNew

interface BoxoContract {

    interface View {

        fun updateUi(list: List<BoxoDto>)

        fun emptyStatesFlow(emptyStates: EmptyStatesEnumNew) // OK nuovo

        fun startDetailsFragment(movieId: Int) // OK nuovo

    }

    interface Presenter {

        fun getMovieAndUpdateUi(forceRefresh: Boolean) // OK nuovo

        fun onVHolderClick(traktMovieId: Int)



    }


}