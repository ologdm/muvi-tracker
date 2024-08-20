package com.example.muvitracker.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.data.dto.basedto.Ids
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
        traktMovieId: Int // TODO ids
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
        showIds: Ids
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetailShowFragment.create(showIds))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    fun startSeasonFragment(
        showIds: Ids,
        seasonNumber: Int
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, SeasonFragment.create(showIds, seasonNumber))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}

