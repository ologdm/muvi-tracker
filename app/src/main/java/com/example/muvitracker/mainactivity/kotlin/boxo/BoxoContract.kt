package com.example.muvitracker.mainactivity.kotlin.boxo

import com.example.muvitracker.repo.kotlin.dto.BoxoDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum

interface BoxoContract {

    interface View {

        fun updateUi(list: List<BoxoDto>)

        fun emptyStatesManagment(emptyStatesEnum: EmptyStatesEnum)

        fun startDetailsFragment(traktMovieId: Int)

    }

    interface Presenter {

        fun onVHolderClick(traktMovieId: Int)

        fun serverCallAndUpdateUi(forceRefresh: Boolean)


    }


}