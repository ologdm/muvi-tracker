package com.example.muvitracker.ui.main.allmovies.base

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhMovieBinding
import com.example.muvitracker.domain.model.MovieItem

class MovieVh(
    val binding: VhMovieBinding
) : RecyclerView.ViewHolder(binding.root) {

    // PRONTA
    fun bind (movie :MovieItem){
        binding.titleVH.text = "${movie.title} (${movie.year})"

        Glide.with(binding.root.context)
            .load(movie.imageUrl())
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.imageVH)
    }

}






