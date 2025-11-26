package com.example.muvitracker.ui.main.prefs.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.ViewholderPrefsMovieBinding
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.ui.main.detailmovie.DetailMovieFragment.MovieDefaults
import com.example.muvitracker.utils.formatToReadableDate
import com.example.muvitracker.utils.orIfNullOrBlank


class PrefsMovieViewholder(
    val binding: ViewholderPrefsMovieBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie, context: Context) {

        binding.run {
            //
            title.text = movie.title.orIfNullOrBlank(MovieDefaults.TITLE)

            //
            // 1° element
            val releaseDate = movie.releaseDate?.formatToReadableDate()
            val releaseDateText = releaseDate.orIfNullOrBlank(MovieDefaults.YEAR)
            // 2° element
            val countryList = movie.countries.filter { it.isNotBlank() }
            val countryText = if (countryList.isNullOrEmpty()){
                MovieDefaults.COUNTRY
            } else {
                "${countryList.joinToString(", ")}"
            }

            //
            val statusText = movie.status?.replaceFirstChar { it.uppercaseChar() }
                .orIfNullOrBlank(MovieDefaults.STATUS)  // es released

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