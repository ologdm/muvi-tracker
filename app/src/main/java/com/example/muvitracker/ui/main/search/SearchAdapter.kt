package com.example.muvitracker.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.domain.model.SearchResult

// predisposition to integrate the series


class SearchAdapter(
    private var onClickVH: (Int) -> Unit,
) : ListAdapter<SearchResult, SearchVH>(SearchAdapter) {

    companion object : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return when {
                oldItem is SearchResult.MovieItem && newItem is SearchResult.MovieItem -> {
                    oldItem.movie.ids.trakt == newItem.movie.ids.trakt
                }

                oldItem is SearchResult.ShowItem && newItem is SearchResult.ShowItem -> {
                    oldItem.show.ids.trakt == newItem.show.ids.trakt
                }

                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }
    }

    // ########################################################################

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VhSearchBinding.inflate(inflater, parent, false)
        return SearchVH(binding)
    }


    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        val item = getItem(position)

        holder.bind(item) // update on VH

        holder.itemView.setOnClickListener {
            when (item) {
                is SearchResult.MovieItem -> onClickVH(item.movie.ids.trakt)
                is SearchResult.ShowItem -> onClickVH(item.show.ids.trakt)
            }
        }


    }

}