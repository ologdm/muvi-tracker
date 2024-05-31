package com.example.muvitracker.inkotlin.ui.main.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.inkotlin.data.dto.search.SearDto
import com.example.muvitracker.inkotlin.utils.toString1stDecimalApproximation

class SearVH(
    val binding: VhSearchBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearDto) {

//        if (item.type == "movie") { // mostra solo i movie, senza
//            binding.run {
//                Glide.with(root.context)
//                    .load(item.movieDto?.getImageUrl())
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .placeholder(R.drawable.glide_placeholder_search)
//                    .error(R.drawable.glide_placeholder_search)
//                    .into(imageVH)
//
//                titleVH.text = "${item.movieDto?.title} ${item.movieDto?.year.toString()}"
//
//                typeObjectVH.text = item.type
//                scoreVH.text = item.score.toString1stDecimalApproximation()
//            }
//        }
    }
}


