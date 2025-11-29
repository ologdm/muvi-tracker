package com.example.muvitracker.ui.main.seasons

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.R
import com.example.muvitracker.data.utils.orIfNull
import com.example.muvitracker.databinding.ViewholderEpisodeOnSeasonBinding
import com.example.muvitracker.domain.model.Episode
import com.example.muvitracker.utils.episodesFormatNumber
import com.example.muvitracker.utils.formatDateFromFirsAired
import com.example.muvitracker.utils.orIfNullOrBlank


class EpisodeViewholder(
    val binding: ViewholderEpisodeOnSeasonBinding
) : ViewHolder(binding.root) {

    private val context: Context = binding.root.context

    // TODO min null, default cases
    fun bind(episode: Episode) {
        binding.episodeInfo.text =
            context.getString(
                R.string.ep_number_first_aired_runtime_min,
                episode.episodeNumber.episodesFormatNumber(),
                episode.firstAiredFormatted.formatDateFromFirsAired().orIfNullOrBlank(
                    context.getString(R.string.date_n_d)),
                episode.runtime.orIfNull("—").toString()
            )

        binding.episodeTitle.text =
            episode.title.orIfNullOrBlank(context.getString(R.string.untitled))

        binding.traktRating.text = episode.traktRating.orIfNullOrBlank("-")
    }

}