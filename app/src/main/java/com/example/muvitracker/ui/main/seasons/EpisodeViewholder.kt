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
        binding.episodeInfo.text =
            "E.${item.episodeNumber.episodesFormatNumber()} • ${item.firstAiredFormatted.formatDateFromFirsAired()} • ${item.runtime} min" // TODO caso null con N/A
//            "Episode ${item.episodeNumber.episodesFormatNumber()}"

        binding.episodeTitle.text =
            "${item.title}"

        binding.traktRating.text = item.traktRating
    }

}