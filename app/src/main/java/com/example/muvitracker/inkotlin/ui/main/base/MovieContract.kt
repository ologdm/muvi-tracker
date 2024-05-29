package com.example.muvitracker.inkotlin.ui.main.base

import com.example.muvitracker.inkotlin.domain.MovieModel
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum


interface MovieContract {

    interface View {

        fun updateUi(list: List<MovieModel>)

        fun handleEmptyStates(emptyStates: EmptyStatesEnum)

        fun startDetailsFragment(movieId: Int)

    }


    interface Presenter {

        fun loadMovieAndUpdateUi(forceRefresh: Boolean)

        fun onVHolderClick(movieId: Int)
    }

}