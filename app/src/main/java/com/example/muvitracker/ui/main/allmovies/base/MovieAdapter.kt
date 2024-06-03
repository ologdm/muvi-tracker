package com.example.muvitracker.ui.main.allmovies.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.databinding.VhMovieBinding
import com.example.muvitracker.domain.model.MovieItem


class MovieAdapter(
    private val onClickCallback: (Int) -> Unit,
) : ListAdapter<MovieItem, MovieVh>(MovieAdapter) {

    companion object : DiffUtil.ItemCallback<MovieItem>() { // nome object - se stesso
        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt // (stesso elemento)
        }

        override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem == newItem // dataclass, quindi vale come equals (contenuto)
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
            onClickCallback(item.ids.trakt)
        }

    }
}


