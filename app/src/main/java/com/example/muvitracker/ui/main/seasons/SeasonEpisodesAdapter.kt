package com.example.muvitracker.ui.main.seasons

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.R
import com.example.muvitracker.databinding.ViewholderEpisodeOnSeasonBinding
import com.example.muvitracker.domain.model.Episode
import com.example.muvitracker.utils.getNowFormattedDateTime

class SeasonEpisodesAdapter(
    val onCLickVH: (Int) -> Unit,
    val onCLickWatched: (Int) -> Unit
) : ListAdapter<Episode, EpisodeViewholder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = ViewholderEpisodeOnSeasonBinding.inflate(layoutInflater, parent, false)
        return EpisodeViewholder(vhBinding)
    }

    override fun onBindViewHolder(holder: EpisodeViewholder, position: Int) {
        val episode = getItem(position)

        holder.bind(episode)

        val context: Context = holder.itemView.context
        val b = holder.binding

        // leggi stato iniziale
        updateWatchedIcon(context, b.watchedIcon, episode = episode)
        b.watchedIcon.setOnClickListener {
            //cambia stato
            onCLickWatched.invoke(episode.episodeNumber)
            // leggi nuovo stato
            updateWatchedIcon(context, b.watchedIcon, episode)
        }


        holder.itemView.setOnClickListener {
            onCLickVH(episode.episodeNumber)
        }
    }


    private fun updateWatchedIcon(
        context: Context,
        watchedIcon: ImageView,
        episode: Episode
    ) {
        val iconEmpty = context.getDrawable(R.drawable.episode_watched_eye_empty)?.mutate()
        val iconFilled = context.getDrawable(R.drawable.episode_watched_eye_filled)

        val nowFormatted = getNowFormattedDateTime()

        val isDisabled = episode.firstAiredFormatted?.let { it > nowFormatted } ?: true
        // !! formato SQLite che permette di paragonare in ordine cronologico

        if (isDisabled) {
            iconEmpty?.alpha = setAlphaForDrawable(0.38f)
            watchedIcon.setImageDrawable(iconEmpty)
        } else {
            watchedIcon.setImageDrawable(if (episode.watched) iconFilled else iconEmpty)
        }
    }


    private fun setAlphaForDrawable(floatAlpha: Float): Int {
        return (floatAlpha * 255).toInt()
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(
                oldItem: Episode,
                newItem: Episode
            ): Boolean {
                return oldItem.episodeTraktId == newItem.episodeTraktId
            }

            override fun areContentsTheSame(
                oldItem: Episode,
                newItem: Episode
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}