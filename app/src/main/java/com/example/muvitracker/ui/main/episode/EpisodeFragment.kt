package com.example.muvitracker.ui.main.episode

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmentEpisodeBottomsheetBinding
import com.example.muvitracker.domain.model.Episode
import com.example.muvitracker.utils.episodesFormatNumber
import com.example.muvitracker.utils.formatDateFromFirsAired
import com.example.muvitracker.utils.getNowFormattedDateTime
import com.example.muvitracker.utils.orDefaultText
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeFragment : BottomSheetDialogFragment(
    R.layout.fragment_episode_bottomsheet
) {

    private var currentShowIds: Ids = Ids()
    private var currentSeasonNr: Int = 0
    private var currentEpisodeNr: Int = 0

    val binding by viewBinding(FragmentEpisodeBottomsheetBinding::bind)
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
        viewModel.state.observe(viewLifecycleOwner) { episode ->
            episode?.let {

                // 1.1.3 ok default
                binding.title.text =
                    getString(
                        R.string.ep_n_episode_title,
                        it.episodeNumber.episodesFormatNumber(), // episodeNumber -1,
                        it.title.orDefaultText(getString(R.string.untitled))
                    )

                // 1.1.3 ok default
                binding.episodeType.text = if (it.episodeType.isNullOrBlank()) {
                    " — "
                } else {
                    "${it.episodeType.replace("_", " ").replaceFirstChar { it.uppercaseChar() }}"
                }


                // 1.1.3 default OK
                binding.episodeInfo.text =
                    "${getString(R.string.release_date)} ${
                        episode.firstAiredFormatted.formatDateFromFirsAired()
                            .orDefaultText("—")
                    }  |  ${
                        getString(
                            R.string.runtime_description,
                            episode.runtime.toString()
                                .orDefaultText(("—"))
                        )
                    } "

                // 1.1.3 ok default
                binding.overviewContent.text =
                    episode.overview.orDefaultText(getString(R.string.not_available))

                // 1.1.3 ok default
                binding.traktRating.text = episode.traktRating.orDefaultText("-")

                updateWatchedIcon(it)
            }
        }

        binding.watchedIcon.setOnClickListener {
            viewModel.toggleWatchedEpisode(
                currentShowIds,
                currentSeasonNr,
                currentEpisodeNr
            )
        }

        loadTMDBImagesWithCustomGlide()
        expandOverview()
    }


    // PRIVATE FUNCTIONS ##########################################
    private fun loadTMDBImagesWithCustomGlide() {
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


    private fun updateWatchedIcon(episode: Episode) {
        val iconEmpty = context?.getDrawable(R.drawable.episode_watched_eye_empty)?.mutate()
        val iconFilled = context?.getDrawable(R.drawable.episode_watched_eye_filled)

        // ! formato SQLite che permette di paragonare in ordine cronologico
        val nowFormatted = getNowFormattedDateTime()
        // CASE: episode.firstAiredFormatted == null  ->  isDisable
        val isDisabled = episode.firstAiredFormatted?.let { it > nowFormatted } ?: true

        if (isDisabled) {
            iconEmpty?.alpha = setAlphaForDrawable(0.38f)
            binding.watchedIcon.setImageDrawable(iconEmpty)
        } else {
            binding.watchedIcon.setImageDrawable(if (episode.watched) iconFilled else iconEmpty)
        }
    }


    private fun setAlphaForDrawable(floatAlpha: Float): Int {
        return (floatAlpha * 255).toInt()
    }

    private fun expandOverview() {
        var isTextExpanded = false // initial state, fragment opening
        binding.overviewContent.setOnClickListener {
            if (isTextExpanded) { // expanded==true -> contract
                binding.overviewContent.maxLines = 5
                binding.overviewContent.ellipsize = TextUtils.TruncateAt.END
            } else { // expanded==false -> expand
                binding.overviewContent.maxLines = Int.MAX_VALUE
                binding.overviewContent.ellipsize = null
            }
            isTextExpanded = !isTextExpanded // toggle state
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