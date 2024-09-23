package com.example.muvitracker.ui.main.prefs.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.databinding.VhPrefsShowBinding
import com.example.muvitracker.domain.model.DetailShow

// espasione titolo max e righe...
// espasione

class PrefsShowVH(
    val binding: VhPrefsShowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(currentItem: DetailShow) {
        binding.run {
            title.text = "${currentItem.title}"
            otherInfo.text =
                "${currentItem.network}, ${currentItem.year}, ${currentItem.country.uppercase()}, ${currentItem.status}"
            // todo passare conteggio
            totalSeasonEpisodes.text =
                " 6 seas, ${currentItem.airedEpisodes} ep"

            watchedCounterProgressBar.max = currentItem.airedEpisodes
            watchedCounterProgressBar.progress = currentItem.watchedCount

            /* !!!!
            - currentItem.airedEpisodes - sia un numero positivo.
            - currentItem.watchedCount - non superi il valore di airedEpisodes.
             */

            // checkbox - su adapter

            Glide
                .with(binding.root.context)
                .load(currentItem.imageUrl())
                .into(image)
        }
    }
}