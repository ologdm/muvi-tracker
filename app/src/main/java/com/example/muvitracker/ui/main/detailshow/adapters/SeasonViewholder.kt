package com.example.muvitracker.ui.main.detailshow.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.R
import com.example.muvitracker.databinding.ViewholderSeasonsOnDetailshowBinding
import com.example.muvitracker.domain.model.Season


class SeasonViewholder(
    val b: ViewholderSeasonsOnDetailshowBinding
) : ViewHolder(b.root) {

    fun bind(seasonItem: Season, context : Context) {
        // old
//        binding.seasonNrAndYear.text = "${context.getString(R.string.season_text)} ${seasonItem.seasonNumber} (${seasonItem.releaseDate?.take(4)})"
        // new 1.1.3  - seson from traslated dto
        b.seasonInfoTextview.text = "${seasonItem.title} (${seasonItem.releaseDate?.take(4)})"
        val episodesText = b.root.context.getString(R.string.episodes_text)
        b.totalEpisodes.text = "${seasonItem.episodeCount} $episodesText"
        b.traktRating.text = seasonItem.traktRating

        b.watchedCounterTextview.text =
            "${seasonItem.watchedCount}/${seasonItem.episodeCount}"

    }

}