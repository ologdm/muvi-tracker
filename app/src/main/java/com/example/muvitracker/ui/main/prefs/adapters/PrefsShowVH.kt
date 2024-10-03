package com.example.muvitracker.ui.main.prefs.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.data.imagetmdb.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.VhPrefsShowBinding
import com.example.muvitracker.domain.model.DetailShow

class PrefsShowVH(
    val binding: VhPrefsShowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(show: DetailShow) {
        binding.run {
            title.text = "${show.title}"
            otherInfo.text =
                "${show.network}, ${show.year}, ${show.country.uppercase()}, ${show.status}"

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