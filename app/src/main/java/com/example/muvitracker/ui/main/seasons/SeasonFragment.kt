package com.example.muvitracker.ui.main.seasons

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
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
class SeasonFragment : Fragment(R.layout.fragm_season_son) {

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


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
            currentSeason = bundle.getInt(SEASON_NUMBER_KEY)
        }


        // SEASON INFO
        viewModel.loadSeasonInfo(showId = currentShowIds.trakt, seasonNumber = currentSeason)

        viewModel.seasonInfoState.observe(viewLifecycleOwner) { stateContainer ->
            val entity = stateContainer.data
            if (entity != null) {
                binding.seasonTitle.text = entity.title
                binding.seasonOverview.text = entity.overview
                updateIconWatchedAll(stateContainer!!.data!!.watchedAll)
            }
        }


        // SEASON EPISODES
        binding.episodesRV.adapter = episodesAdapter
        binding.episodesRV.layoutManager = LinearLayoutManager(requireContext())

        viewModel.loadSeasonEpisodes(showId = currentShowIds.trakt, seasonNumber = currentSeason)

        viewModel.seasonEpisodesState.observe(viewLifecycleOwner) { stateContainer ->
            episodesAdapter.submitList(stateContainer.data)
        }


        //  WATCHED_ALL_ICON  0000
//        viewModel.isWatchedAllSeasonEpisodes(currentShowIds.trakt, currentSeason)

        // todo - sposto su
//        viewModel.isWatchedAllSeasonEpisodesStatus.observe(viewLifecycleOwner) { isWatched ->
//            updateIconWatchedAll(isWatched)
//        }

        binding.watchedAllIcon.setOnClickListener {
            viewModel.toggleSeasonAllWatchedEpisodes(currentShowIds.trakt, currentSeason)
        }


        // IMAGE vertical - poster
        viewModel.getTmdbImageLinksFlow(
            showTmdbId = currentShowIds.tmdb,
            seasonNr = currentSeason
        )

//        viewModel.getTmdbTEST(currentShowIds.tmdb, currentSeason)

        viewModel.posterImageUrl.observe(viewLifecycleOwner) { posterUrl ->
            Glide.with(requireContext())
                .load(posterUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(binding.seasonPoster)
        }


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

    }


    // CAMBIA ICONA COLORE
    private fun updateIconWatchedAll(isWatched: Boolean) {
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