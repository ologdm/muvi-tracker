package com.example.muvitracker.ui.main.detailshow.adapters

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding
import com.example.muvitracker.domain.model.SeasonExtended


class SeasonVH(
    val binding: VhSeasonsOnDetailshowBinding
) : ViewHolder(binding.root) {

    //    fun bind (seasonExtenDto : SeasonExtenDto){
    fun bind(seasonItem: SeasonExtended) {
        binding.seasonNrAndYear.text = "${seasonItem.title} (${seasonItem.releaseYear})"
        val episodesText = binding.root.context.getString(R.string.episodes_text)
        binding.totalEpisodes.text = "${seasonItem.episodeCount} $episodesText"
        binding.traktRating.text = seasonItem.rating

        // totale - usare episodeCount 00
        binding.watchedCounterTextview.text =
            "${seasonItem.watchedCount}/${seasonItem.episodeCount}"

    }

}