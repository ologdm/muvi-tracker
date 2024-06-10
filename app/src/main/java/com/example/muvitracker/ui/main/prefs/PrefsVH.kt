package com.example.muvitracker.ui.main.prefs

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.databinding.VhPrefsBinding
import com.example.muvitracker.domain.model.DetailMovie


class PrefsVH(
    val binding: VhPrefsBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(currentItem: DetailMovie) {
        binding.run {
            title.text = currentItem.title
            year.text = currentItem.year.toString()

            Glide
                .with(binding.root.context)
                .load(currentItem.imageUrl())
                .into(image)
        }
    }
}