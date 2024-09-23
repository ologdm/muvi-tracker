package com.example.muvitracker.ui.main.prefs.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.databinding.VhPrefsMovieBinding
import com.example.muvitracker.domain.model.DetailMovie


class PrefsMovieVH(
    val binding: VhPrefsMovieBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(currentItem: DetailMovie) {
        binding.run {
            title.text = currentItem.title
            otherInfo.text = "${currentItem.year} (${currentItem.country})"

            Glide
                .with(binding.root.context)
                .load(currentItem.imageUrl())
                .into(image)
        }
    }
}