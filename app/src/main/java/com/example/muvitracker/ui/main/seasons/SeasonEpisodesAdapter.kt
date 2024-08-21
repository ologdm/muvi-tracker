package com.example.muvitracker.ui.main.seasons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto.EpisodeExtenDto
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.databinding.VhEpisodeOnseasonBinding
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding

class SeasonEpisodesAdapter(
    val onCLickVH: (Int) -> Unit
) : ListAdapter<EpisodeExtenDto, EpisodeVH>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = VhEpisodeOnseasonBinding.inflate(layoutInflater, parent, false)
        return EpisodeVH(vhBinding)
    }

    override fun onBindViewHolder(holder: EpisodeVH, position: Int) {
        val episode = getItem(position)

        holder.bind(episode)

        holder.itemView.setOnClickListener {
            onCLickVH(episode.number)
        }

    }


    // OK
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EpisodeExtenDto>() {
            override fun areItemsTheSame(
                oldItem: EpisodeExtenDto,
                newItem: EpisodeExtenDto
            ): Boolean {
                return oldItem.ids.trakt == newItem.ids.trakt
            }

            override fun areContentsTheSame(
                oldItem: EpisodeExtenDto,
                newItem: EpisodeExtenDto
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}