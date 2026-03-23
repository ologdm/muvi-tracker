package com.example.presentation.prefs.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.presentation.detailmovie.DetailMovieFragment.MovieDefaults
import com.example.core.formatToReadableDate
import com.example.core.orDefaultText
import com.example.domain.ImageTmdbRequest
import com.example.domain.model.Movie
import com.example.presentation.R
import com.example.presentation.databinding.ViewholderPrefsMovieBinding


class PrefsMovieViewholder(
    val binding: ViewholderPrefsMovieBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie, context: Context) {

        binding.run {
            //
            title.text = movie.title.orDefaultText(MovieDefaults.TITLE)

            //
            // 1° element
            val releaseDate = movie.releaseDate?.formatToReadableDate()
            val releaseDateText = releaseDate.orDefaultText(MovieDefaults.YEAR)
            // 2° element
            val countryList = movie.countries.filter { it.isNotBlank() }
            val countryText = if (countryList.isNullOrEmpty()){
                MovieDefaults.COUNTRY
            } else {
                "${countryList.joinToString(", ")}"
            }

            //
            val statusText = movie.status?.replaceFirstChar { it.uppercaseChar() }
                .orDefaultText(MovieDefaults.STATUS)  // es released

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