package com.example.muvitracker.ui.main.prefs.viewpager


import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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
                0 -> getString(R.string.movies)
                1 -> getString(R.string.shows)
//                2 -> getString(R.string.people) TODO 1.1.4
                else -> null
            }
        }.attach()

        mainLayoutTopEdgeToEdgeManagment()
    }

    private fun mainLayoutTopEdgeToEdgeManagment() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // aggiorno solo lati che mi servono
            v.updatePadding(top = systemBars.top)
            insets
        }
    }




}