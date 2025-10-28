package com.example.muvitracker.ui.main.prefs.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.VhPrefsShowBinding
import com.example.muvitracker.domain.model.Show

class PrefsShowVH(
    val binding: VhPrefsShowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(show: Show) {
        binding.run {
            title.text = "${show.title}"
            otherInfo.text =
                "${show.networks.joinToString(", ")}, ${show.year}, ${show.countries.joinToString(", ").uppercase()}, ${show.status}"

            Glide
                .with(binding.root.context)
                .load(ImageTmdbRequest.ShowVertical(show.ids.tmdb))
                .into(image)

            // update watched states
            watchedCounterProgressBar.max = show.airedEpisodes
            watchedCounterProgressBar.progress = show.watchedCount
            watchedCounterTextview.text = "${show.watchedCount}/${show.airedEpisodes}"

        }
    }
}