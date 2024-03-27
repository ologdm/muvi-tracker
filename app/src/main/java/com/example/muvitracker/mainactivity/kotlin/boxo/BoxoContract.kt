package com.example.muvitracker.mainactivity.kotlin.boxo

import com.example.muvitracker.repo.kotlin.dto.BoxoDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnumNew

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