package com.example.muvitracker.ui.main.seasons

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.databinding.FragmSeasonSonBinding
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
class SeasonFragment private constructor() : Fragment(R.layout.fragm_season_son) {

    private var currentShowIds: Ids = Ids()
    private var currentSeason: Int = 0

    private val binding by viewBinding(FragmSeasonSonBinding::bind)
    private val viewModel by viewModels<SeasonViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val episodesAdapter = SeasonEpisodesAdapter(
        onCLickVH = { episodeNumber ->
            navigator.startEpisodeFragment(currentShowIds, currentSeason, episodeNumber)
        },
        onCLickWatched = { episodeTraktId ->
            viewModel.toggleWatchedEpisode(currentShowIds.trakt, currentSeason, episodeTraktId)
        }
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.episodesRV.adapter = episodesAdapter
        binding.episodesRV.layoutManager = LinearLayoutManager(requireContext())

        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
            currentSeason = bundle.getInt(SEASON_NUMBER_KEY)
        }

        // 00
        viewModel.seasonInfoState.observe(viewLifecycleOwner) { stateContainer ->
            val entity = stateContainer.data
            if (entity != null) {
                binding.seasonTitle.text = entity.title
                binding.seasonOverview.text = entity.overview
                updateWatchedAllIcon(stateContainer.data!!.watchedAll)
            }
        }
        viewModel.loadSeasonInfo(showId = currentShowIds.trakt, seasonNumber = currentSeason)

        // 00
        viewModel.seasonEpisodesState.observe(viewLifecycleOwner) { stateContainer ->
            println("LLLL EPISODES${stateContainer.data}")
            episodesAdapter.submitList(stateContainer.data)
        }
        viewModel.loadSeasonEpisodes(showId = currentShowIds.trakt, seasonNumber = currentSeason)


        // Espansione e riduzione overview - OK
        var isTextExpanded = false // initial state, fragment opening
        binding.seasonOverview.setOnClickListener {
            if (isTextExpanded) { // expanded==true -> contract
                binding.seasonOverview.maxLines = 6
                binding.seasonOverview.ellipsize = TextUtils.TruncateAt.END
            } else { // expanded==false -> expand
                binding.seasonOverview.maxLines = Int.MAX_VALUE
                binding.seasonOverview.ellipsize = null
            }
            isTextExpanded = !isTextExpanded // toggle state
        }


        // TODO !!!!!!!!!!!!!!!!!!!!!!!
        binding.watchedAllIcon.setOnClickListener {
            viewModel.toggleWatchedAllEpisodes(currentShowIds.trakt, currentSeason)
            // aggiorna db su episode, season, TODO show
        }

    }


    // CAMBIA ICONA COLORE
    private fun updateWatchedAllIcon(isWatched: Boolean) {
//        val iconEmpty = context?.getDrawable(R.drawable.watched_check_circle_empty)
//        val iconFilled = context?.getDrawable(R.drawable.watched_check_circle_filled)
        val iconEmpty = context?.getDrawable(R.drawable.x_checkbox_multiple_blank_circle_outline)
        val iconFilled = context?.getDrawable(R.drawable.x_checkbox_multiple_blank_circle)

        binding.watchedAllIcon.setImageDrawable(if (isWatched) iconFilled else iconEmpty)
    }

    companion object {
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