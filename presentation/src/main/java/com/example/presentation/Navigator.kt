package com.example.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.domain.model.Ids
import com.example.presentation.detailmovie.DetailMovieFragment
import com.example.presentation.detailshow.DetailShowFragment
import com.example.presentation.episode.EpisodeFragment
import com.example.presentation.person.PersonBottomSheetFragment
import com.example.presentation.person.PersonFragment
import com.example.presentation.seasons.viewpager.SeasonViewpagerFragment
import javax.inject.Inject

class Navigator @Inject constructor(
    private val fragmentActivity: FragmentActivity
) {

    fun replaceFragment(
        fragment: Fragment
    ) {
        val manager = fragmentActivity.supportFragmentManager
        // fix r1.1.2
        // popBackStack - elimina tutti i fragment dalla backstack, solo quelli aggiunti con .addToBackStack()
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        manager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }


    fun startMovieDetailFragment(
        movieIds: Ids
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetailMovieFragment.Companion.create(movieIds))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    fun startShowDetailFragment(
        showIds: Ids
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetailShowFragment.Companion.create(showIds))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) // setTransition solo animazioni standard
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
                SeasonViewpagerFragment.Companion.create(showTitle, showIds, seasonNumber, totSeasonsNumber)
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
        val episodeFragment = EpisodeFragment.Companion.create(showIds, seasonNumber, episodeNumber)
        episodeFragment.show(fragmentActivity.supportFragmentManager, "EpisodeFragment")
        // replace,commit non necessari -> show() fa gia tutto internamente
    }


    // PERSON DETAIL
    // 1. from cast
    fun startPersonFragmentFromCast(
        personIds: Ids,
        character: String
    ) {
        val personFragment = PersonBottomSheetFragment.Companion.create(personIds, character).apply {
            show(fragmentActivity.supportFragmentManager, "PersonFragmentCast")
        }
    }

    // 2. from search
    fun startPersonFragmentFromSearch(
        personIds: Ids
    ) {
        fragmentActivity.supportFragmentManager.beginTransaction()
            .replace(
                R.id.frameLayout,
                PersonFragment.Companion.create(personIds)
            )
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


}