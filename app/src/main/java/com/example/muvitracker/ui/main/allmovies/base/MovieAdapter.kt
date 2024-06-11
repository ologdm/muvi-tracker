package com.example.muvitracker.ui.main.allmovies.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.databinding.VhMovieBinding
import com.example.muvitracker.domain.model.base.Movie


class MovieAdapter(
    private val onClickVH: (Int) -> Unit,
) : ListAdapter<Movie, MovieVh>(MovieAdapter) {

    companion object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVh {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhMovieBinding.inflate(layoutInflater, parent, false)
        return MovieVh(bindingVh)
    }


    override fun onBindViewHolder(holder: MovieVh, position: Int) {
        val item = getItem(position)

        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickVH(item.ids.trakt)
        }

    }
}


