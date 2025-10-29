package com.example.muvitracker.ui.main.detailmovie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.ViewholderRelatedListOnDetailBinding
import com.example.muvitracker.domain.model.base.MovieBase

class RelatedMoviesAdapter(
    private val onClickVH: (Ids) -> Unit,
) : ListAdapter<MovieBase, RelatedMovieViewholder>(RelatedMoviesAdapter) {


    companion object : DiffUtil.ItemCallback<MovieBase>() {
        override fun areItemsTheSame(oldItem: MovieBase, newItem: MovieBase): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: MovieBase, newItem: MovieBase): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedMovieViewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = ViewholderRelatedListOnDetailBinding.inflate(layoutInflater, parent, false)
        return RelatedMovieViewholder(bindingVh)
    }

    override fun onBindViewHolder(holder: RelatedMovieViewholder, position: Int) {
        val item = getItem(position)

        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickVH(item.ids)
        }
    }


}