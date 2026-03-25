package com.example.presentation.person.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.domain.glide.ImageTmdbRequest
import com.example.domain.model.CastMember
import com.example.presentation.R
import com.example.presentation.databinding.ViewholderCastListOnDetailBinding


class CastViewholder(
    val binding: ViewholderCastListOnDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(castMember: CastMember) {

        binding.personName.text = "${castMember.personBase.name}"
        binding.characterName.text = "${castMember.character}"

        Glide.with(binding.root.context)
            .load(ImageTmdbRequest.Person(castMember.personBase.ids.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)
    }

}









