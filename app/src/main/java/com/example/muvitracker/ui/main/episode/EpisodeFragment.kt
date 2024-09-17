package com.example.muvitracker.ui.main.episode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.FragmEpisodeBottomsheetBinding
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeFragment private constructor() :
    BottomSheetDialogFragment(R.layout.fragm_episode_bottomsheet) {

    private var currentShowIds: Ids = Ids()
    private var currentSeason: Int = 0
    private var currentEpisode: Int = 0

    val binding by viewBinding(FragmEpisodeBottomsheetBinding::bind)
    val viewmodel by viewModels<EpisodeViewmodel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // OK
        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
            currentSeason = bundle.getInt(SEASON_NUMBER_KEY)
            currentEpisode = bundle.getInt(EPISODE_NUMBER_KEY)
        }

        viewmodel.state.observe(viewLifecycleOwner) { episodeDto ->
            binding.title.text = episodeDto.title
            binding.info.text = episodeDto.firstAired
            binding.overview.text = episodeDto.overview
        }


//        Glide.with(requireContext())
        //        TODO


    }


    companion object {

        fun create(
            showIds: Ids,
            seasonNumber: Int,
            episodeNumber: Int
        ): EpisodeFragment {
            val episodeFragment = EpisodeFragment()
            val bundle = Bundle()
            bundle.putParcelable(SHOW_IDS_KEY, showIds)
            bundle.putInt(SEASON_NUMBER_KEY, seasonNumber)
            bundle.putInt(EPISODE_NUMBER_KEY, episodeNumber)
            episodeFragment.arguments = bundle
            return episodeFragment
        }

        private const val SHOW_IDS_KEY = "showIdsKey"
        private const val SEASON_NUMBER_KEY = "seasonNumber"
        private const val EPISODE_NUMBER_KEY = "episodeNumber"

    }


}