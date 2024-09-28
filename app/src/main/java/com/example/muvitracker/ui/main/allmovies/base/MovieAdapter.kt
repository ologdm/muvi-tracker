package com.example.muvitracker.ui.main.allmovies.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.VhBaseListBinding

import com.example.muvitracker.domain.model.base.Movie

// used by:
// - boxoffice fragment (only 10 result, no paging needed)

class MovieAdapter(
    val onClickVH: (Ids) -> Unit
) : ListAdapter<Movie, MovieVH>(MovieAdapter) {

    companion object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhBaseListBinding.inflate(layoutInflater, parent, false)
        return MovieVH(bindingVh)
    }


    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val item = getItem(position)

        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickVH(item.ids)
        }

    }
}