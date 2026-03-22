package com.example.presentation.seasons.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.domain.model.Ids
import com.example.presentation.seasons.SeasonFragment

class SeasonViewpagerAdapter(
    fragment: Fragment,
    val ids: Ids,
    private val seasonCount: Int
) : androidx.viewpager2.adapter.FragmentStateAdapter(fragment) {


    // position==index==0, like in lists
    override fun createFragment(position: Int): Fragment {
        return SeasonFragment.create(ids, position + 1)
    }
    // ?????????


    override fun getItemCount(): Int {
        return seasonCount
    }


}