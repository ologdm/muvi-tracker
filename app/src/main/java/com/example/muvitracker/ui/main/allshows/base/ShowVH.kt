package com.example.muvitracker.ui.main.allshows.base

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhBaseListBinding
import com.example.muvitracker.domain.model.base.Show

class ShowVH(
    private val vhBinding: VhBaseListBinding
) : RecyclerView.ViewHolder(vhBinding.root) {

    fun bind (show : Show){
        vhBinding.title.text = show.title

        Glide.with(vhBinding.root.context)
            .load(show.imageUrl())
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(vhBinding.image)
    }
}






