package com.example.muvitracker.ui.main.seasons

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.muvitracker.data.dto.basedto.Ids

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