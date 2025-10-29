package com.example.muvitracker.ui.main.prefs.viewpager


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmentPrefsViewpagerBinding
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PrefsViewpagerFragment :Fragment(R.layout.fragment_prefs_viewpager){

    private val binding by viewBinding(FragmentPrefsViewpagerBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = PrefsViewpagerAdapter(this)

        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Movies"
                1 -> "Shows"
                else -> null
            }
        }.attach()

    }


}