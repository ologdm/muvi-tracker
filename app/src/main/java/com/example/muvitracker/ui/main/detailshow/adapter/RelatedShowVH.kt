package com.example.muvitracker.ui.main.detailshow.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhRelatedListBinding
import com.example.muvitracker.domain.model.base.Show

class RelatedShowVH(
    val binding: VhRelatedListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(show: Show) {
        Glide.with(binding.root.context)
            .load(show.imageUrl())
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)

        println("YYY related  viewholder:$show")
    }
}









