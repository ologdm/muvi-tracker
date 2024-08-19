package com.example.muvitracker.ui.main.seasons

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmSeasonBinding
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

// viewmodel
//    - call season info
//    - call episodes ok
// state ok
// adapter TODO
// tab - pageviewer2 TODO
// recyclerview


@AndroidEntryPoint
class SeasonFragment private constructor() : Fragment(R.layout.fragm_season) {

    // TODO layout
    private var currentShowId: Int = 0
    private var currentSeason: Int = 0
//    private var currentSeasonItem: SeasonExtenDto? = null

    private val binding by viewBinding(FragmSeasonBinding::bind)
    private val viewModel by viewModels<SeasonViewmodel>()

    // adapter // TODO

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val bundle = arguments
        if (bundle != null) {
            currentShowId = bundle.getInt(SHOW_ID_KEY)
            currentSeason = bundle.getInt(SEASON_NUMBER_KEY)
//            currentSeasonItem = bundle.getParcelable(SHOW_ID_KEY) // parcelable
        }

        viewModel.seasonInfoState.observe(viewLifecycleOwner) { stateContainer ->
            // TODO updateUi
        }

        viewModel.seasonEpisodesState.observe(viewLifecycleOwner) { stateContainer ->
            // TODO adapter update
        }


        // OK
        viewModel.loadSeasonInfo(showId = currentShowId, seasonNumber = currentSeason)
        viewModel.loadSeasonEpisodes(showId = currentShowId, seasonNumber = currentSeason)



        // TODO - swipe on tabs - change seasons

    }


    // di che dati ho bisogno?
    companion object {
        // ARGUMENT
        fun create(
            traktId: Int,
            seasonNumber: Int, // get from db
//            seasonItem: SeasonExtenDto // togliere parcelable
        ): SeasonFragment {
            val seasonFragment = SeasonFragment()
            val bundle = Bundle()
            bundle.putInt(SHOW_ID_KEY, traktId)
//            bundle.putParcelable(SEASON_ITEM, seasonItem)
            bundle.putInt(SEASON_NUMBER_KEY, seasonNumber)
            seasonFragment.arguments = bundle

            return seasonFragment
        }

        private const val SHOW_ID_KEY = "traktShowIdKey"
        private const val SEASON_NUMBER_KEY = "seasonNumber"
//        private const val SEASON_ITEM = "seasonItem"
    }

}