package com.example.muvitracker.ui.main.allshows.base

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.VhBaseListBinding
import com.example.muvitracker.domain.model.base.ShowBase

class ShowVH(
    private val vhBinding: VhBaseListBinding
) : RecyclerView.ViewHolder(vhBinding.root) {

    fun bind (showBase : ShowBase){
        vhBinding.title.text = showBase.title

        Glide.with(vhBinding.root.context)
            .load(ImageTmdbRequest.ShowVertical(showBase.ids.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(vhBinding.image)
    }
}