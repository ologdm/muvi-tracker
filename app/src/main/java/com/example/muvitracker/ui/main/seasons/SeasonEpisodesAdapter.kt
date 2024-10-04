package com.example.muvitracker.ui.main.seasons

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.R
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.databinding.VhEpisodeOnseasonBinding

class SeasonEpisodesAdapter(
    val onCLickVH: (Int) -> Unit,
    val onCLickWatched: (Int) -> Unit
) : ListAdapter<EpisodeEntity, EpisodeVH>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = VhEpisodeOnseasonBinding.inflate(layoutInflater, parent, false)
        return EpisodeVH(vhBinding)
    }

    override fun onBindViewHolder(holder: EpisodeVH, position: Int) {
        val currentItem = getItem(position)

        // icons
        val context: Context = holder.itemView.context
        val iconFilled = context.getDrawable(R.drawable.episode_watched_eye_filled)
        val iconEmpty = context.getDrawable(R.drawable.episode_watched_eye_empty)


        holder.binding.run {
            // leggi stato iniziale
            updateWatchedIcon(watchedIcon, currentItem.watched, iconFilled, iconEmpty) // 00

            watchedIcon.setOnClickListener {
                //cambia stato
                onCLickWatched.invoke(currentItem.episodeNumber)
                // leggi nuovo stato
                updateWatchedIcon(watchedIcon, currentItem.watched, iconFilled, iconEmpty)

            }

        }


        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            onCLickVH(currentItem.episodeNumber)
        }

    }


    private fun updateWatchedIcon(
        watchedIcon: ImageView,
        isWatched: Boolean,
        iconFilled: Drawable?,
        iconEmpty: Drawable?
    ) {
        if (isWatched)
            watchedIcon.setImageDrawable(iconFilled)
        else
            watchedIcon.setImageDrawable(iconEmpty)
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