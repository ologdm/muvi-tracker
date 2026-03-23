package com.example.presentation.seasons

import android.content.Context
import com.example.core.episodesFormatNumber
import com.example.core.formatDateFromFirsAired
import com.example.core.orDefaultText
import com.example.domain.model.Episode
import com.example.presentation.R
import com.example.presentation.databinding.ViewholderEpisodeOnSeasonBinding


class EpisodeViewholder(
    val binding: ViewholderEpisodeOnSeasonBinding
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    private val context: Context = binding.root.context

    // TODO min null, default cases
    fun bind(episode: Episode) {
        binding.episodeInfo.text =
            context.getString(
                R.string.ep_number_first_aired_runtime_min,
                episode.episodeNumber.episodesFormatNumber(),
                episode.firstAiredFormatted.formatDateFromFirsAired()
                    .orDefaultText(context.getString(R.string.date_n_d)),
                episode.runtime.orDefaultText("—")
            )

        binding.episodeTitle.text =
            episode.title
                .orDefaultText(context.getString(R.string.untitled))

        binding.traktRating.text = episode.traktRating.orDefaultText("-")
    }

}