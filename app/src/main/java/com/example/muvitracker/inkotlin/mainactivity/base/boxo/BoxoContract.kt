package com.example.muvitracker.inkotlin.mainactivity.base.boxo

import com.example.muvitracker.inkotlin.repo.dto.base.BoxoDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum

interface BoxoContract {

    interface View {

        fun updateUi(list: List<BoxoDto>)

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum) // OK nuovo

        fun startDetailsFragment(movieId: Int) // OK nuovo

    }

    interface Presenter {

        fun getMovieAndUpdateUi(forceRefresh: Boolean) // OK nuovo

        fun onVHolderClick(traktMovieId: Int)



    }


}