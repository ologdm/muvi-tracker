package com.example.presentation.prefs.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.core.formatToReadableDate
import com.example.core.orDefaultText
import com.example.domain.glide.ImageTmdbRequest
import com.example.domain.model.Movie
import com.example.presentation.R
import com.example.presentation.databinding.ViewholderPrefsMovieBinding
import com.example.presentation.detailmovie.MovieDefaults


class PrefsMovieViewholder(
    val binding: ViewholderPrefsMovieBinding
) : RecyclerView.ViewHolder(binding.root) {

    val defaults = MovieDefaults(binding.root.context)


    fun bind(movie: Movie) {
        binding.run {

            title.text = movie.title.orDefaultText(defaults.TITLE)

            //
            // 1° element
            val releaseDate = movie.releaseDate?.formatToReadableDate()
            val releaseDateText = releaseDate.orDefaultText(defaults.YEAR)
            // 2° element
            val countryList = movie.countries.filter { it.isNotBlank() }
            val countryText = if (countryList.isNullOrEmpty()) {
                defaults.COUNTRY
            } else {
                "${countryList.joinToString(", ")}"
            }


            val statusText = movie.status?.replaceFirstChar { it.uppercaseChar() }
                .orDefaultText(defaults.STATUS)  // es released

            // 1+2
            otherInfo.text = "$releaseDateText  |  $countryText  |  $statusText"

            Glide
                .with(binding.root.context)
                .load(ImageTmdbRequest.MovieVertical(movie.ids.tmdb))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(image)
        }
    }
}