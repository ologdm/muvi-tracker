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
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmSeasonSonBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SeasonFragment : Fragment(R.layout.fragm_season_son) {

    private var currentShowIds: Ids = Ids()
    private var currentSeasonNr: Int = 0

    private val binding by viewBinding(FragmSeasonSonBinding::bind)
    private val viewModel by viewModels<SeasonViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val episodesAdapter = SeasonEpisodesAdapter(
        onCLickVH = { episodeNumber ->
            navigator.startEpisodeFragment(currentShowIds, currentSeasonNr, episodeNumber)
        },
        onCLickWatched = { episodeTraktId ->
            viewModel.toggleWatchedEpisode(currentShowIds.trakt, currentSeasonNr, episodeTraktId)
        }
    )


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
            currentSeasonNr = bundle.getInt(SEASON_NUMBER_KEY)
        }


        // SEASON INFO
        viewModel.loadSeasonInfo(showId = currentShowIds.trakt, seasonNumber = currentSeasonNr)

        viewModel.seasonInfoState.observe(viewLifecycleOwner) { stateContainer ->
            val entity = stateContainer.data
            if (entity != null) {
                binding.seasonTitle.text = entity.title
                binding.seasonOverview.text = entity.overview
                updateIconWatchedAll(entity.watchedAll)
            }
        }


        // SEASON EPISODES
        binding.episodesRV.adapter = episodesAdapter
        binding.episodesRV.layoutManager = LinearLayoutManager(requireContext())

        viewModel.loadSeasonEpisodes(showId = currentShowIds.trakt, seasonNumber = currentSeasonNr)

        viewModel.seasonEpisodesState.observe(viewLifecycleOwner) { stateContainer ->
            episodesAdapter.submitList(stateContainer.data)
        }

        binding.watchedAllIcon.setOnClickListener {
            viewModel.toggleSeasonAllWatchedEpisodes(currentShowIds.trakt, currentSeasonNr)
        }


        // IMAGE TMDB - custom glide
            Glide.with(requireContext())
                .load(ImageTmdbRequest.Season(currentShowIds.tmdb,currentSeasonNr))
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(binding.seasonPoster)



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
        val iconEmpty = context?.getDrawable(R.drawable.episode_watched_all_empty)
        val iconFilled = context?.getDrawable(R.drawable.episode_watched_all_filled)
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