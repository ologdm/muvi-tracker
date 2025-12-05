package com.example.muvitracker.ui.main.allmovies.base

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.ViewholderExploreBaseBinding
import com.example.muvitracker.domain.model.base.MovieBase

class MovieViewholder(
    val binding: ViewholderExploreBaseBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieBase: MovieBase) {
        binding.title.text = "${movieBase.title} (${movieBase.year})"

        // TODO TEST
        // Thumbnail aggiornato usando RequestBuilder invece del float deprecato
//        val thumbRequest = Glide.with(binding.root.context)
//            .load(ImageTmdbRequest.MovieVertical(movie.ids.tmdb))
//            .override(200, 300) // dimensione ridotta per la thumbnail
//            .placeholder(R.drawable.glide_placeholder_base)
//            .error(R.drawable.glide_placeholder_base)

        Glide.with(binding.root.context)
            // posso passare il dato custom - in load()
            .load(ImageTmdbRequest.MovieVertical(movieBase.ids.tmdb))
//            .thumbnail(thumbRequest)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)

        // TODO 1.1.3 preloading,
        Glide.with(binding.root.context)
            .load(ImageTmdbRequest.MovieHorizontal(movieBase.ids.tmdb))
            .preload()
    }
}








