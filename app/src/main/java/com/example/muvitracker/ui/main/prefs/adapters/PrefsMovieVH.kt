package com.example.muvitracker.ui.main.prefs.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.VhPrefsMovieBinding
import com.example.muvitracker.domain.model.DetailMovie


class PrefsMovieVH(
    val binding: VhPrefsMovieBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(movie: DetailMovie) {
        binding.run {
            title.text = movie.title
            otherInfo.text = "${movie.year} (${movie.country.uppercase()})"

            Glide
                .with(binding.root.context)
                .load(ImageTmdbRequest.MovieVertical(movie.ids.tmdb))
                .into(image)
        }
    }
}