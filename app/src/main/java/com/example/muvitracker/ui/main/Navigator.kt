package com.example.muvitracker.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.ui.main.detailmovie.DetailMovieFragment
import com.example.muvitracker.ui.main.detailshow.DetailShowFragment
import com.example.muvitracker.ui.main.episode.EpisodeFragment
import com.example.muvitracker.ui.main.person.PersonFragment
import com.example.muvitracker.ui.main.seasons.viewpager.SeasonViewpagerFragment
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
        movieIds: Ids // TODO ids
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetailMovieFragment.create(movieIds))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


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

    fun startSeasonsViewpagerFragment(
        showTitle: String,
        showIds: Ids,
        seasonNumber: Int,
        totSeasonsNumber: Int
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(
                R.id.frameLayout,
                SeasonViewpagerFragment.create(showTitle, showIds, seasonNumber, totSeasonsNumber)
            )
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    fun startEpisodeFragment(
        showIds: Ids,
        seasonNumber: Int,
        episodeNumber: Int
    ) {
        val episodeFragment = EpisodeFragment.create(showIds, seasonNumber, episodeNumber)
        episodeFragment.show(fragmentActivity.supportFragmentManager, "EpisodeFragment")
        // replace,commit non necessari -> show() fa gia tutto internamente
    }


    // PERSON DETAIL
    // 1. from cast
    fun startPersonFragmentFromCast(
        personIds: Ids,
        character: String
    ) {
        val personFragment = PersonFragment.create(personIds, character).apply {
            show(fragmentActivity.supportFragmentManager, "PersonFragmentCast")
        }
    }

    // 2. from search
    fun startPersonFragmentFromSearch(
        personIds: Ids
    ) {
        val personFragment = PersonFragment.create(personIds).apply {
            show(fragmentActivity.supportFragmentManager, "PersonFragmentSearch")
        }
    }


}

