package com.example.muvitracker.ui.main.seasons

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.databinding.FragmSeasonBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// viewmodel
//    - call season info, after from db
//    - call episodes ok
// state ok
// adapter TODO
// tab - pageviewer2 TODO
// recyclerview


@AndroidEntryPoint
class SeasonFragment private constructor() : Fragment(R.layout.fragm_season) {

    // TODO layout
    private var currentShowIds: Ids = Ids()
    private var currentSeason: Int = 0

    private val binding by viewBinding(FragmSeasonBinding::bind)
    private val viewModel by viewModels<SeasonViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val adapter = SeasonEpisodesAdapter(onCLickVH = { episodeIds ->
//        navigator.startEpisodeFragment() TODO
    })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
            currentSeason = bundle.getInt(SEASON_NUMBER_KEY)
        }

        viewModel.seasonInfoState.observe(viewLifecycleOwner) { stateContainer ->
            // TODO updateUi
        }

        viewModel.seasonEpisodesState.observe(viewLifecycleOwner) { stateContainer ->
            // TODO adapter update
        }


        // OK
        viewModel.loadSeasonInfo(showId = currentShowIds.trakt, seasonNumber = currentSeason)
        viewModel.loadSeasonEpisodes(showId = currentShowIds.trakt, seasonNumber = currentSeason)


        // TODO - swipe on tabs - change seasons

    }


    companion object {
        // ARGUMENT
        fun create(
            showIds: Ids, // OK
            seasonNumber: Int, // get from db
        ): SeasonFragment {
            val seasonFragment = SeasonFragment()
            val bundle = Bundle()
            bundle.putParcelable(SHOW_IDS_KEY, showIds)
            bundle.putInt(SEASON_NUMBER_KEY, seasonNumber)
            seasonFragment.arguments = bundle
            return seasonFragment
        }

        private const val SHOW_IDS_KEY = "showIdsKey"
        private const val SEASON_NUMBER_KEY = "seasonNumber"
    }
}