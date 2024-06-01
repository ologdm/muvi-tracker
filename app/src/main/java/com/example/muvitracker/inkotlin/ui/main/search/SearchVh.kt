package com.example.muvitracker.inkotlin.ui.main.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.inkotlin.data.dto.SearchDto
import com.example.muvitracker.inkotlin.utils.firstDecimalApproxToString

class SearchVh(
    val binding: VhSearchBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchDto) {

        binding.run {
            typeObjectVH.text = item.type
            scoreVH.text = item.score.firstDecimalApproxToString()

            if (item.type == "movie") { // mostra solo i movie, senza
                titleVH.text = "${item.movie?.title} ${item.movie?.year.toString()}"

                Glide.with(root.context)
                    .load(item.movie?.imageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.glide_placeholder_search)
                    .error(R.drawable.glide_placeholder_search)
                    .into(imageVH)
            }
        }
    }

}


