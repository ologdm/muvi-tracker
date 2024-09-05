package com.example.muvitracker.ui.main.prefs.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.muvitracker.ui.main.prefs.PrefsFragment


class PrefsViewpagerAdapter (
    fragment: Fragment
) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return 2 // 2 tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PrefsFragment() // movie
            1 -> PrefsFragment() // show
            else -> throw IllegalStateException("Invalid position $position")
        }
    }


}