package com.example.muvitracker.ui.main.seasons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.dto.EpisodeExtenDto
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.databinding.VhEpisodeOnseasonBinding
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding

class SeasonEpisodesAdapter(
    val onCLickVH: (Int) -> Unit,
    val watchedCLick : (Boolean) -> Unit
) : ListAdapter<EpisodeEntity, EpisodeVH>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = VhEpisodeOnseasonBinding.inflate(layoutInflater, parent, false)
        return EpisodeVH(vhBinding)
    }

    override fun onBindViewHolder(holder: EpisodeVH, position: Int) {
        val episode = getItem(position)

        holder.bind(episode)

        holder.itemView.setOnClickListener {
            onCLickVH(episode.episodeNumber)
        }

    }


    // OK
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EpisodeEntity>() {
            override fun areItemsTheSame(
                oldItem: EpisodeEntity,
                newItem: EpisodeEntity
            ): Boolean {
                return oldItem.episodeTraktId == newItem.episodeTraktId
            }

            override fun areContentsTheSame(
                oldItem: EpisodeEntity,
                newItem: EpisodeEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}