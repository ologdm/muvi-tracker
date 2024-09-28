package com.example.muvitracker.ui.main.detailshow.adapters

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.utils.firstDecimalApproxToString


class SeasonVH(
    val binding : VhSeasonsOnDetailshowBinding
) :ViewHolder(binding.root) {

//    fun bind (seasonExtenDto : SeasonExtenDto){
    fun bind (seasonItem : SeasonExtended){
        binding.seasonNrAndYear.text = "${seasonItem.title} (${seasonItem.releaseYear})"
        binding.totalEpisodes.text = "${seasonItem.episodeCount} episodes"
        binding.rating.text = seasonItem.rating?.firstDecimalApproxToString()

    // totale - usare episodeCount 00
        binding.watchedCounterTextview.text = "${seasonItem.watchedCount}/${seasonItem.episodeCount}"

    }

}