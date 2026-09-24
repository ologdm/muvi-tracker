package com.example.presentation.allmovies.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.domain.glide.ImageTmdbRequest
import com.example.domain.model.base.MovieBase
import com.example.presentation.R
import com.example.presentation.databinding.ViewholderExploreBaseBinding

class MovieViewholder(
    val binding: ViewholderExploreBaseBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieBase: MovieBase) {
        binding.title.text = "${movieBase.title} (${movieBase.year})"

        Glide.with(binding.root.context)
            .load(ImageTmdbRequest.MovieVertical(movieBase.ids.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)

        // 1.1.3 preloading OK
        Glide.with(binding.root.context)
            .load(ImageTmdbRequest.MovieHorizontal(movieBase.ids.tmdb))
            .preload()
    }
}








