package com.example.muvitracker.ui.main.search.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.ViewholderSearchBinding
import com.example.muvitracker.domain.model.SearchResult

class SearchResultViewholder(
    val binding: ViewholderSearchBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: SearchResult) {

        binding.run {

            when (item) {
                is SearchResult.MovieItem -> {
                    typeItem.text = root.context.getString(R.string.movies)
//                    score.text = item.score.firstDecimalApproxToString()
                    title.text = "${item.movieBase?.title} (${item.movieBase?.year.toString()})"

                    Glide.with(root.context)
                        .load(ImageTmdbRequest.MovieVertical(item.movieBase.ids.tmdb))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.glide_placeholder_search)
                        .error(R.drawable.glide_placeholder_search)
                        .into(image)
                }

                is SearchResult.ShowItem -> {
                    typeItem.text = root.context.getString(R.string.shows)
//                    score.text = item.score.firstDecimalApproxToString()
                    title.text = "${item.showBase.title} (${item.showBase.year})"

                    Glide.with(root.context)
                        .load(ImageTmdbRequest.ShowVertical(item.showBase.ids.tmdb))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.glide_placeholder_search)
                        .error(R.drawable.glide_placeholder_search)
                        .into(image)
                }

                is SearchResult.PersonItem -> {
                    typeItem.text = root.context.getString(R.string.people)
                    title.text = "${item.personBase.name}"

                    Glide.with(root.context)
                        .load(ImageTmdbRequest.Person(item.personBase.ids.tmdb))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(image)
                }
            }
        }
    }
}