package com.example.muvitracker.ui.main.seasons

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.databinding.VhEpisodeOnseasonBinding
import com.example.muvitracker.domain.model.EpisodeExtended
import com.example.muvitracker.utils.episodesFormatNumber
import com.example.muvitracker.utils.formatDateFromFirsAired

class EpisodeVH(
    val binding: VhEpisodeOnseasonBinding
) : ViewHolder(binding.root) {


    fun bind(item: EpisodeExtended) {
        binding.episodeNumberAndRelease.text =
            "E.${item.episodeNumber.episodesFormatNumber()} • ${item.firstAiredFormatted.formatDateFromFirsAired()}" // TODO caso null con N/A

        binding.episodeTitleAndRuntime.text =
            "${item.title} (${item.runtime} min)"

        binding.traktRating.text = item.traktRating
    }

}