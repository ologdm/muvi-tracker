package com.example.muvitracker.ui.main.seasons

import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.DetailShowDto
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.databinding.FragmSeasonViewpagerBinding
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SeasonViewpagerFragment private constructor() : Fragment(R.layout.fragm_season_viewpager) {

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


        // TODO viewpager2
        // TODO viewpager2
        // TODO viewpager2
        // TODO viewpager2

        binding.viewPager.adapter =
            SeasonViewpagerAdapter(
                fragment = this,
                ids = currentShowIds,
                seasonCount = totalSeasons
            ) // pesca da db


        // TODO tab mediator
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Season ${position + 1}"
        }

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