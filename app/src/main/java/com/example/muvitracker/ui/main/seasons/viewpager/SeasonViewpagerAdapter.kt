package com.example.muvitracker.ui.main.seasons.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.ui.main.seasons.SeasonFragment

class SeasonViewpagerAdapter(
    fragment: Fragment,
    val ids: Ids,
    private val seasonCount: Int
) : FragmentStateAdapter(fragment) {


    // position==index==0, like in lists
    override fun createFragment(position: Int): Fragment {
        return SeasonFragment.create(ids, position + 1)
    }
    // ?????????


    override fun getItemCount(): Int {
        return seasonCount
    }


}