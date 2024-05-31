package com.example.muvitracker.inkotlin.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.inkotlin.data.dto.search.SearDto
import com.example.muvitracker.inkotlin.utils.toString1stDecimalApproximation


class SearAdapter2(
    private var onClickCallback: (Int) -> Unit,
) : ListAdapter<SearDto, SearVH>(SearAdapter2) {


    companion object : DiffUtil.ItemCallback<SearDto>() {
        override fun areItemsTheSame(oldItem: SearDto, newItem: SearDto): Boolean {
            return oldItem.movieDto?.ids?.trakt ==
                    newItem.movieDto?.ids?.trakt
        }

        override fun areContentsTheSame(oldItem: SearDto, newItem: SearDto): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearVH {
        val binding = VhSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearVH(binding)
    }


    override fun onBindViewHolder(holder: SearVH, position: Int) {
        val item = getItem(position)

//        holder.bind(item) // update on VH
        with(holder.binding) {
            typeObjectVH.text = item.type
            scoreVH.text = item.score.toString1stDecimalApproximation()

            if (item.movieDto != null) { // mostra solo i movie, senza
                titleVH.text = "${item.movieDto.title} ${item.movieDto.year}"

                Glide.with(root.context)
                    .load(item.movieDto.imageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.glide_placeholder_search)
                    .error(R.drawable.glide_placeholder_search)
                    .into(imageVH)

            }
        }



        holder.itemView.setOnClickListener {
            onClickCallback.invoke(item.movieDto?.ids!!.trakt) // TODO check nullable
        }
    }

}