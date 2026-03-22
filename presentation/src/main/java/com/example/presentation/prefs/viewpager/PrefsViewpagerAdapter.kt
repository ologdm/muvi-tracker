package com.example.presentation.prefs.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.presentation.prefs.PrefsMoviesFragment
import com.example.presentation.prefs.PrefsPersonFragment
import com.example.presentation.prefs.PrefsShowsFragment


class PrefsViewpagerAdapter (
    fragment: Fragment
) : androidx.viewpager2.adapter.FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PrefsMoviesFragment() // movie
            1 -> PrefsShowsFragment() // show
            2 -> PrefsPersonFragment() // person >TODO
            else -> throw IllegalStateException("Invalid position $position")
        }
    }


}