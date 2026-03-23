package com.example.presentation.detailmovie.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.domain.ImageTmdbRequest
import com.example.domain.model.base.MovieBase
import com.example.presentation.R
import com.example.presentation.databinding.ViewholderRelatedListOnDetailBinding


class RelatedMovieViewholder(
    val binding: ViewholderRelatedListOnDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieBase: MovieBase) {
        Glide.with(binding.root.context)
            .load(ImageTmdbRequest.MovieVertical(movieBase.ids.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)
    }
}









