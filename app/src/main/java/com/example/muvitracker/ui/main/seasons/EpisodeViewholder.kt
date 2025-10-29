package com.example.muvitracker.ui.main.seasons

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.databinding.ViewholderEpisodeOnSeasonBinding
import com.example.muvitracker.domain.model.Episode
import com.example.muvitracker.utils.episodesFormatNumber
import com.example.muvitracker.utils.formatDateFromFirsAired


class EpisodeViewholder(
    val binding: ViewholderEpisodeOnSeasonBinding
) : ViewHolder(binding.root) {


    fun bind(item: Episode) {
        binding.episodeNumberAndRelease.text =
            "E.${item.episodeNumber.episodesFormatNumber()} • ${item.firstAiredFormatted.formatDateFromFirsAired()}" // TODO caso null con N/A

        binding.episodeTitleAndRuntime.text =
            "${item.title} (${item.runtime} min)"

        binding.traktRating.text = item.traktRating
    }

}