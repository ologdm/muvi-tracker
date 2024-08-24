package com.example.muvitracker.ui.main.seasons

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.databinding.VhEpisodeOnseasonBinding
import com.example.muvitracker.utils.firstDecimalApproxToString

class EpisodeVH(
    val binding: VhEpisodeOnseasonBinding
) : ViewHolder(binding.root) {

    fun bind(item: EpisodeEntity) {
        binding.episodeNumberAndRelease.text = "#${item.episodeNumber} • ${item.firstAiredFormatted}"
        binding.episodeTitleAndRuntime.text = "${item.title} (${item.runtime} min)"
        binding.rating.text = item.rating.firstDecimalApproxToString()
    }

}