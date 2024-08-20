package com.example.muvitracker.ui.main.seasons

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.data.dto.EpisodeExtenDto
import com.example.muvitracker.databinding.VhEpisodeOnseasonBinding
import com.example.muvitracker.utils.firstDecimalApproxToString

class EpisodeVH(
    val binding: VhEpisodeOnseasonBinding
) : ViewHolder(binding.root) {

    fun bind(dto: EpisodeExtenDto) {
        binding.episodeNumberAndRelease.text = "#${dto.number} • ${dto.getDateFromFirsAired()} "
        binding.episodeTitleAndRuntime.text = "${dto.title} (${dto.runtime} min)"
        binding.rating.text = dto.rating.firstDecimalApproxToString()
    }

    // TODO watched click

}