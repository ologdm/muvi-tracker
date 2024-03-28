package com.example.muvitracker.inkotlin.mainactivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.mainactivity.deta_movie.DetaFragmentB

// kotlin
// supportFragmentManager - senza() perche e un getter, metodo implicito


// OK
class MainNavigatorK {


    // 1.
    fun replaceFragment(
        fragmentActivity: FragmentActivity,
        fragment: Fragment
    ) {
        val manager: FragmentManager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }


    // 2.
    fun addToBackStackFragment(
        fragmentActivity: FragmentActivity,
        fragment: Fragment
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }


    // 3.
    fun startDetailsFragmentAndAddToBackstack(
        fragmentActivity: FragmentActivity,
        traktMovieId: Int
    ) {
        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetaFragmentB.create(traktMovieId))
            .addToBackStack(null)
            .commit()
    }



    // TODO separate
    // 3.
    fun startDetaMovFragmentAndAddToBackstack(
        fragmentActivity: FragmentActivity,
        traktMovieId: Int
    ) {
        val type = "movie"

        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetaFragmentB.createWithShow(traktMovieId, type))
            .addToBackStack(null)
            .commit()
    }

    // separate
    // 3.
    fun startDetaShowFragmentAndAddToBackstack(
        fragmentActivity: FragmentActivity,
        traktMovieId: Int
    ) {
        val type = "show"

        val manager = fragmentActivity.supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetaFragmentB.createWithShow(traktMovieId,type))
            .addToBackStack(null)
            .commit()
    }


}