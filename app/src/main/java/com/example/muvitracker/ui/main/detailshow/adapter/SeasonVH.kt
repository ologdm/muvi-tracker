package com.example.muvitracker.ui.main.detailshow.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding
import com.example.muvitracker.utils.firstDecimalApproxToString


class SeasonVH(
    val binding : VhSeasonsOnDetailshowBinding
) :ViewHolder(binding.root) {

    fun bind (seasonExtenDto : SeasonExtenDto){
        binding.seasonNrAndYear.text = "${seasonExtenDto.title} (${seasonExtenDto.getYear()})"
        binding.totalEpisodes.text = "${seasonExtenDto.airedEpisodes} episodes"
        binding.rating.text = seasonExtenDto.rating.firstDecimalApproxToString()

//        binding.episodesCount.text = // TODO

    }

}