package com.example.muvitracker.ui.main.person.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.VhCastListOnDetailBinding
import com.example.muvitracker.domain.model.CastMember

class CastVH(
    val binding: VhCastListOnDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(castMember: CastMember) {

        binding.personName.text = "${castMember.personBase.name}"
        binding.characterName.text = "${castMember.character}"

        Glide.with(binding.root.context)
            .load(ImageTmdbRequest.Person(castMember.personBase.ids.tmdb ?: 0))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)
    }

}









