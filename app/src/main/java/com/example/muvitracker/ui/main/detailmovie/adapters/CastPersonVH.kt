package com.example.muvitracker.ui.main.detailmovie.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.xperson.PersonExtendedDto
import com.example.muvitracker.data.imagetmdb.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.VhRelatedListOnDetailBinding
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.model.base.Show

class CastPersonVH(
    val binding: VhRelatedListOnDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(personExtendedDto: PersonExtendedDto) {
        Glide.with(binding.root.context)
            .load(ImageTmdbRequest.Person(personExtendedDto.ids.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)
    }
}









