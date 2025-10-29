package com.example.muvitracker.ui.main.prefs.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.ViewholderPrefsMovieBinding
import com.example.muvitracker.domain.model.Movie


class PrefsMovieViewholder(
    val binding: ViewholderPrefsMovieBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(movie: Movie) {
        binding.run {
            title.text = movie.title
            otherInfo.text = "${movie.year} (${movie.countries.joinToString(", ").uppercase()})"

            Glide
                .with(binding.root.context)
                .load(ImageTmdbRequest.MovieVertical(movie.ids.tmdb))
                .into(image)
        }
    }
}