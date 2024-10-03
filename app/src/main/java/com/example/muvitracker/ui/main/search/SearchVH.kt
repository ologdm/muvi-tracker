package com.example.muvitracker.ui.main.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.imagetmdb.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.domain.model.SearchResult
import com.example.muvitracker.utils.firstDecimalApproxToString

class SearchVH(
    val binding: VhSearchBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: SearchResult) {

        binding.run {

            when (item) {
                is SearchResult.MovieItem -> {
                    typeItem.text = root.context.getString(R.string.movie)
//                    score.text = item.score.firstDecimalApproxToString()
                    title.text = "${item.movie?.title} ${item.movie?.year.toString()}"

                    Glide.with(root.context)
                        .load(ImageTmdbRequest.MovieVertical(item.movie.ids.tmdb))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.glide_placeholder_search)
                        .error(R.drawable.glide_placeholder_search)
                        .into(image)
                }

                is SearchResult.ShowItem -> {
                    typeItem.text = root.context.getString(R.string.show)
//                    score.text = item.score.firstDecimalApproxToString()
                    title.text = "${item.show.title} ${item.show.year.toString()}"

                    Glide.with(root.context)
                        .load(ImageTmdbRequest.ShowVertical(item.show.ids.tmdb))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.glide_placeholder_search)
                        .error(R.drawable.glide_placeholder_search)
                        .into(image)
                }
            }
        }
    }
}



