package com.example.muvitracker.ui.main.detailshow.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding


class SeasonVH(
    val binding : VhSeasonsOnDetailshowBinding
) :ViewHolder(binding.root) {

    fun bind (seasonExtenDto : SeasonExtenDto){
//        binding.seasonNrAndYear.text
        binding.totalEpisodes.text = "${seasonExtenDto.title} (${seasonExtenDto.airedEpisodes})"
    }

}