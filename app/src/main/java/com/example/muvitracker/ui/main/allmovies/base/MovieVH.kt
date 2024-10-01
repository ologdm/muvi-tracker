package com.example.muvitracker.ui.main.allmovies.base

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.imagetmdb.glide.ImageRequest
import com.example.muvitracker.databinding.VhBaseListBinding
import com.example.muvitracker.domain.model.base.Movie

class MovieVH(
    val binding: VhBaseListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.title.text = "${movie.title} (${movie.year})"

        Glide.with(binding.root.context)
            // TODO posso passare il dato custom onn load
//            .load(ImageRequest.Movie(movie.ids.tmdb))
            .load(movie.imageUrl())
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)
    }
}








