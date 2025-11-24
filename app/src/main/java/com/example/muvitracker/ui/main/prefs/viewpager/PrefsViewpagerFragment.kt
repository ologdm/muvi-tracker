package com.example.muvitracker.ui.main.prefs.viewpager


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmentPrefsViewpagerBinding
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.core.content.edit
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrefsViewpagerFragment : Fragment(R.layout.fragment_prefs_viewpager) {

    private val b by viewBinding(FragmentPrefsViewpagerBinding::bind)

    @Inject
    lateinit var sharedPrefs: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.viewPager.adapter = PrefsViewpagerAdapter(this)
        b.viewPager.setCurrentItem(getLastTab(), false)

        b.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                saveLastTab(position) // salva su shared
            }
        })


        b.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        TabLayoutMediator(b.tabLayout, b.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.movies)
                1 -> getString(R.string.shows)
//                2 -> getString(R.string.people) TODO 1.1.4
                else -> null
            }
        }.attach()


        mainLayoutTopEdgeToEdgeManagment()
    }


    private fun saveLastTab(position: Int) {
        sharedPrefs.edit {
            putInt("last_tab_prefs", position)
        }
    }


    private fun getLastTab(): Int {
        return sharedPrefs
            .getInt("last_tab_prefs", 0) // default = 0
    }

    private fun mainLayoutTopEdgeToEdgeManagment() {
        ViewCompat.setOnApplyWindowInsetsListener(b.mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // aggiorno solo lati che mi servono
            v.updatePadding(top = systemBars.top)
            insets
        }
    }
}