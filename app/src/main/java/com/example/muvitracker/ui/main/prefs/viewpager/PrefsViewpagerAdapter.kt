package com.example.muvitracker.ui.main.prefs.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.muvitracker.ui.main.prefs.PrefsMovieFragment
import com.example.muvitracker.ui.main.prefs.PrefsShowFragment


class PrefsViewpagerAdapter (
    fragment: Fragment
) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return 2 // 2 tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PrefsMovieFragment() // movie
            1 -> PrefsShowFragment() // show
            else -> throw IllegalStateException("Invalid position $position")
        }
    }


}