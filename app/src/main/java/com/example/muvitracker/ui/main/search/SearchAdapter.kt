package com.example.muvitracker.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.domain.model.SearchResult

// predisposition to integrate the series


class SearchAdapter(
    private var onClickVHMovie: (Ids) -> Unit,
    private var onClickVHShow: (Ids) -> Unit,
    private var onClickVHPerson: (Ids) -> Unit
) : PagingDataAdapter<SearchResult, SearchVH>(SearchAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VhSearchBinding.inflate(inflater, parent, false)
        return SearchVH(binding)
    }


    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        val item = getItem(position) ?: return

        holder.bind(item)

        holder.itemView.setOnClickListener {
            when (item) {
                is SearchResult.MovieItem -> onClickVHMovie(item.movie.ids)
                is SearchResult.ShowItem -> onClickVHShow(item.show.ids)
                is SearchResult.PersonItem ->  onClickVHPerson(item.person.ids)
            }
        }
    }


    companion object : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return when {
                oldItem is SearchResult.MovieItem && newItem is SearchResult.MovieItem -> {
                    oldItem.movie.ids.trakt == newItem.movie.ids.trakt
                }

                oldItem is SearchResult.ShowItem && newItem is SearchResult.ShowItem -> {
                    oldItem.show.ids.trakt == newItem.show.ids.trakt
                }

                oldItem is SearchResult.PersonItem && newItem is SearchResult.PersonItem -> {
                    oldItem.person.ids.trakt == newItem.person.ids.trakt
                }

                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }
    }

}