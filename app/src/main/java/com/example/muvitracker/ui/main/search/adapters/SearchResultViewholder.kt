package com.example.muvitracker.ui.main.search.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.ViewholderSearchBinding
import com.example.muvitracker.domain.model.SearchResult
import com.example.muvitracker.ui.main.search.SearchFragment

class SearchResultViewholder(
    val binding: ViewholderSearchBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchResult, currentFilter: String) {
        // TODO 1.1.3 - visibilità type -> solo se 'all' selez - OK
        binding.typeItem.visibility = if (currentFilter == SearchFragment.MOVIE_SHOW_PERSON) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.run {
            when (item) {
                is SearchResult.MovieItem -> {
                    typeItem.text = root.context.getString(R.string.movies)
//                    score.text = item.score.firstDecimalApproxToString(
                    val movie = item.movieBase

                    // TODO 1.1.3: filtrare su repo ricerca elementi senza titolo
                    title.text = if (movie.title.isBlank()) {
                        "Unknown"
                    } else if (movie.year == -1) { // manca year  && c'e title
                        "${movie.title}"
                    } else {
                        "${movie.title} (${movie.year})"
                    }

                    Glide.with(root.context)
                        .load(ImageTmdbRequest.MovieVertical(movie.ids.tmdb))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.glide_placeholder_base)
                        .error(R.drawable.glide_placeholder_base)
                        .into(image)
                }

                is SearchResult.ShowItem -> {
                    typeItem.text = root.context.getString(R.string.shows)
//                    score.text = item.score.firstDecimalApproxToString()
                    val show = item.showBase
                    title.text = if (show.title.isBlank()) {
                        "Unknown"
                    } else if (show.year == -1) { // year never null
                        "${show.title}"
                    } else {
                        "${show.title} (${show.year})"
                    }

                    Glide.with(root.context)
                        .load(ImageTmdbRequest.ShowVertical(item.showBase.ids.tmdb))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.glide_placeholder_base)
                        .error(R.drawable.glide_placeholder_base)
                        .into(image)
                }

                is SearchResult.PersonItem -> {
                    typeItem.text = root.context.getString(R.string.people)

                    val person = item.personBase
                    title.text = if (person.name.isBlank()){
                        "Unknown"
                    } else {
                        "${item.personBase.name}"
                    }

                    Glide.with(root.context)
                        .load(ImageTmdbRequest.Person(item.personBase.ids.tmdb))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.glide_placeholder_base)
                        .error(R.drawable.glide_placeholder_base)
                        .into(image)
                }
            }
        }
    }
}