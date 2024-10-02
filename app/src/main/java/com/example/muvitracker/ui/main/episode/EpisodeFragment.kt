package com.example.muvitracker.ui.main.episode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.imagetmdb.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmEpisodeBottomsheetBinding
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeFragment : BottomSheetDialogFragment(R.layout.fragm_episode_bottomsheet) {

    private var currentShowIds: Ids = Ids()
    private var currentSeasonNr: Int = 0
    private var currentEpisodeNr: Int = 0

    val binding by viewBinding(FragmEpisodeBottomsheetBinding::bind)
    private val viewModel by viewModels<EpisodeViewmodel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // get bundle
        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
            currentSeasonNr = bundle.getInt(SEASON_NUMBER_KEY)
            currentEpisodeNr = bundle.getInt(EPISODE_NUMBER_KEY)
        }

        // load episode
        viewModel.loadEpisode(
            showTraktId = currentShowIds.trakt,
            seasonNr = currentSeasonNr,
            episodeNr = currentEpisodeNr
        )
        viewModel.state.observe(viewLifecycleOwner) { episodeEntity ->
            binding.title.text = episodeEntity.title
            binding.info.text = episodeEntity.firstAiredFormatted
            binding.overview.text = episodeEntity.overview
        }


        // load episode image - from tmdb
        viewModel.getTmdbImageLinksFlow(
            showTmdbId = currentShowIds.tmdb,
            seasonNr = currentSeasonNr,
            episodeNr = currentEpisodeNr
        )

        viewModel.backdropImageUrl.observe(viewLifecycleOwner) { backdropUrl ->
            Glide.with(requireContext())
                .load(
                    ImageTmdbRequest.Episode(
                        currentShowIds.tmdb,
                        currentSeasonNr,
                        currentEpisodeNr
                    )
                ) // 1399 game-of-thrones
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(binding.episodeImageBackdrop)
        }

    }


    companion object {
        fun create(showIds: Ids, seasonNr: Int, episodeNr: Int): EpisodeFragment {
            val episodeFragment = EpisodeFragment()
            val bundle = Bundle()
            bundle.putParcelable(SHOW_IDS_KEY, showIds)
            bundle.putInt(SEASON_NUMBER_KEY, seasonNr)
            bundle.putInt(EPISODE_NUMBER_KEY, episodeNr)
            episodeFragment.arguments = bundle
            return episodeFragment
        }

        private const val SHOW_IDS_KEY = "showIdsKey"
        private const val SEASON_NUMBER_KEY = "seasonNumber"
        private const val EPISODE_NUMBER_KEY = "episodeNumber"

    }


}