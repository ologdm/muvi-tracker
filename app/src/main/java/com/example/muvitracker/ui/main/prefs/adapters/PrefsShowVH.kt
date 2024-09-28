package com.example.muvitracker.ui.main.prefs.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.databinding.VhPrefsShowBinding
import com.example.muvitracker.domain.model.DetailShow

class PrefsShowVH(
    val binding: VhPrefsShowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(currentItem: DetailShow) {
        binding.run {
            title.text = "${currentItem.title}"
            otherInfo.text =
                "${currentItem.network}, ${currentItem.year}, ${currentItem.country.uppercase()}, ${currentItem.status}"

            Glide
                .with(binding.root.context)
                .load(currentItem.imageUrl())
                .into(image)

            // update watched states
            watchedCounterProgressBar.max = currentItem.airedEpisodes
            watchedCounterProgressBar.progress = currentItem.watchedCount
            watchedCounterTextview.text = "${currentItem.watchedCount}/${currentItem.airedEpisodes}"

        }
    }
}