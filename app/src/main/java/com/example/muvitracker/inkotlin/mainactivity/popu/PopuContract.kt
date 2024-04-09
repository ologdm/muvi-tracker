package com.example.muvitracker.inkotlin.mainactivity.popu

import com.example.muvitracker.inkotlin.mainactivity.base.base_repo.MovieModel
import com.example.muvitracker.inkotlin.repo.dto.PopuDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum

interface PopuContract {


    interface View {

        fun updateUi(list: List<MovieModel>)

        fun startDetailsFragment(movieId: Int)

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum)

    }


    interface Presenter {

        fun getMovieAndUpdateUi(forceRefresh: Boolean)

        fun onVHolderCLick(movieId: Int)
    }

}