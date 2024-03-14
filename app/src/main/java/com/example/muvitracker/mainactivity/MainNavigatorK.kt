package com.example.muvitracker.mainactivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.muvitracker.R
import com.example.muvitracker.mainactivity.details.DetailsFragment

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
            .replace(R.id.frameLayout, DetailsFragment.create(traktMovieId))
            .commit()
    }


}