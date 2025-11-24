package com.example.muvitracker.ui.main.seasons.viewpager

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.muvitracker.R
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.FragmentSeasonViewpagerBinding
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SeasonViewpagerFragment : Fragment(R.layout.fragment_season_viewpager) {

    private var currentShowTitle: String = ""
    private var currentShowIds: Ids = Ids()
    private var currentSeason: Int = 0
    private var totalSeasons: Int = 0

    private val b by viewBinding(FragmentSeasonViewpagerBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        if (bundle != null) {
            currentShowTitle = bundle.getString(SHOW_TITLE_KEY) ?: ""
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
            currentSeason = bundle.getInt(SEASON_NUMBER_KEY)
            totalSeasons = bundle.getInt(TOT_SEASONS_NUMBER_KEY)
        }

        mainLayoutTopEdgeToEdgeManagement()

        b.showTitle.text = currentShowTitle

        b.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


        // viewpager2 00
        b.viewPager.adapter =
            SeasonViewpagerAdapter(
                fragment = this,
                ids = currentShowIds,
                seasonCount = totalSeasons
            )

        // Imposta il ViewPager2 sulla stagione corretta 00
        // indice come nelle liste,-1 rispetto alla conteggio naturale
        b.viewPager.setCurrentItem(currentSeason - 1, false)

        // tab mediator 00
        b.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(b.tabLayout, b.viewPager) { tab, position ->
            val seasonText = getString(R.string.season_text)
            tab.text = "$seasonText ${position + 1}"
        }.attach()

    }

    private fun mainLayoutTopEdgeToEdgeManagement() {
        ViewCompat.setOnApplyWindowInsetsListener(b.mainLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                topMargin = systemBars.top
                topMargin = (systemBars.top - dpToPx(8)).coerceAtLeast(0) // non andare sotto 0
            }
            insets
        }

    }

    // funzione helper per convertire dp in px
    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
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