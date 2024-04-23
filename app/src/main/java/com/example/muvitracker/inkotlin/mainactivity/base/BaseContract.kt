package com.example.muvitracker.inkotlin.mainactivity.base

import com.example.muvitracker.inkotlin.model.MovieModel
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum


interface BaseContract {


    interface View {

        fun updateUi(list: List<MovieModel>)

        fun startDetailsFragment(movieId: Int)

        fun emptyStatesFlow(emptyStates: EmptyStatesEnum)

    }


    interface Presenter {

        fun getMovieAndUpdateUi(forceRefresh: Boolean)

        fun onVHolderClick(movieId: Int)
    }

}