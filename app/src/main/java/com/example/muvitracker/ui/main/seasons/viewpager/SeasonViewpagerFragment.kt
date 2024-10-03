package com.example.muvitracker.ui.main.seasons.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.FragmSeasonViewpagerBinding
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SeasonViewpagerFragment : Fragment(R.layout.fragm_season_viewpager) {

    private var currentShowTitle: String = ""
    private var currentShowIds: Ids = Ids()
    private var currentSeason: Int = 0
    private var totalSeasons: Int = 0

    private val binding by viewBinding(FragmSeasonViewpagerBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        if (bundle != null) {
            currentShowTitle = bundle.getString(SHOW_TITLE_KEY) ?: ""
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
            currentSeason = bundle.getInt(SEASON_NUMBER_KEY)
            totalSeasons = bundle.getInt(TOT_SEASONS_NUMBER_KEY)
        }

        binding.showTitle.text = currentShowTitle

        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


        // viewpager2 00
        binding.viewPager.adapter =
            SeasonViewpagerAdapter(
                fragment = this,
                ids = currentShowIds,
                seasonCount = totalSeasons
            )

        // Imposta il ViewPager2 sulla stagione corretta 00
        // indice come nelle liste,-1 rispetto alla conteggio naturale
        binding.viewPager.setCurrentItem(currentSeason - 1, false)

        // tab mediator 00
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val seasonText = getString(R.string.season_text)
            tab.text = "$seasonText ${position + 1}"
        }.attach()

    }


    companion object {
        fun create(
            showTitle: String,
            showIds: Ids,
            seasonNumber: Int,
            totSeasonsNumber: Int,
        ): SeasonViewpagerFragment {
            val seasonFragment = SeasonViewpagerFragment()
            val bundle = Bundle()
            bundle.putString(SHOW_TITLE_KEY, showTitle)
            bundle.putParcelable(SHOW_IDS_KEY, showIds)
            bundle.putInt(SEASON_NUMBER_KEY, seasonNumber)
            bundle.putInt(TOT_SEASONS_NUMBER_KEY, totSeasonsNumber)
            seasonFragment.arguments = bundle
            return seasonFragment
        }

        private const val SHOW_TITLE_KEY = "showTitleKey"
        private const val SHOW_IDS_KEY = "showIdsKey"
        private const val SEASON_NUMBER_KEY = "seasonNumber"
        private const val TOT_SEASONS_NUMBER_KEY = "totSeasonsNumber"
    }
}