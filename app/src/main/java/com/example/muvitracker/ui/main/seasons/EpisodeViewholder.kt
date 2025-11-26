package com.example.muvitracker.ui.main.seasons

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.muvitracker.R
import com.example.muvitracker.databinding.ViewholderEpisodeOnSeasonBinding
import com.example.muvitracker.domain.model.Episode
import com.example.muvitracker.utils.episodesFormatNumber
import com.example.muvitracker.utils.formatDateFromFirsAired


class EpisodeViewholder(
    val binding: ViewholderEpisodeOnSeasonBinding
) : ViewHolder(binding.root) {

    private val context: Context = binding.root.context


    fun bind(item: Episode) {
        binding.episodeInfo.text =
            context.getString(
                R.string.ep_number_first_aired_runtime_min,
                item.episodeNumber.episodesFormatNumber(),
                item.firstAiredFormatted.formatDateFromFirsAired(),
                item.runtime
            ) // TODO caso null con N/A
//            "Episode ${item.episodeNumber.episodesFormatNumber()}"

        binding.episodeTitle.text =
            "${item.title}"

        binding.traktRating.text = item.traktRating
    }

}