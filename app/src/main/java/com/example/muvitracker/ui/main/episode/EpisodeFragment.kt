package com.example.muvitracker.ui.main.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.basedto.Ids
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EpisodeFragment private constructor() : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_episode_bottom_sheet, container, false)
    }












    companion object {

        fun create (showIds: Ids, seasonNumber :Int, episodeNumber :Int
        ): EpisodeFragment{
            val episodeFragment = EpisodeFragment()
            val bundle = Bundle()
            bundle.putParcelable(SHOW_IDS_KEY,showIds)
            bundle.putInt(SEASON_NUMBER_KEY, seasonNumber)
            bundle.putInt(EPISODE_NUMBER_KEY, episodeNumber)
            episodeFragment.arguments = bundle
            return episodeFragment
        }

        private const val SHOW_IDS_KEY = "showIdsKey"
        private const val SEASON_NUMBER_KEY = "seasonNumber"
        private const val EPISODE_NUMBER_KEY = "seasonNumber"

    }


}