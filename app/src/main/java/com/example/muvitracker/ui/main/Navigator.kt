package com.example.muvitracker.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.ui.main.detailmovie.DetailMovieFragment
import com.example.muvitracker.ui.main.detailmovie.DetailShowFragment
import com.example.muvitracker.ui.main.seasons.SeasonFragment
import javax.inject.Inject


class Navigator @Inject constructor(
    private val fragmentActivity: FragmentActivity
) {

    fun replaceFragment(
        fragment: Fragment
    ) {
        val manager: FragmentManager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }


    fun startMovieDetailFragment(
        traktMovieId: Int
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetailMovieFragment.create(traktMovieId))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    // TODO OK
    fun startShowDetailFragment(
        traktShowId: Int
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetailShowFragment.create(traktShowId))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    fun startSeasonFragment(
        traktShowId: Int,
        seasonNumber: Int
//        seasonDto : SeasonExtenDto,
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, SeasonFragment.create(traktShowId, seasonNumber))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}

