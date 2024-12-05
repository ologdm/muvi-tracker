package com.example.muvitracker.ui.main.seasons

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.databinding.VhEpisodeOnseasonBinding
import com.example.muvitracker.utils.episodesFormatNumber
import com.example.muvitracker.utils.formatDateFromFirsAired

class EpisodeVH(
    val binding: VhEpisodeOnseasonBinding
) : ViewHolder(binding.root) {

    fun bind(item: EpisodeEntity) {

        binding.episodeNumberAndRelease.text =
            "E.${item.episodeNumber.episodesFormatNumber()} • ${item.firstAiredFormatted?.formatDateFromFirsAired() ?: "N/A"}"
        binding.episodeTitleAndRuntime.text =
            "${item.title} (${item.runtime} min)"
        binding.traktRating.text = item.rating
    }

}