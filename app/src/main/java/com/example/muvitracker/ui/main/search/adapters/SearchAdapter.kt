package com.example.muvitracker.ui.main.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.ViewholderSearchBinding
import com.example.muvitracker.domain.model.SearchResult
import com.example.muvitracker.ui.main.search.adapters.SearchViewholder

class SearchAdapter(
    private var onClickVHMovie: (Ids) -> Unit,
    private var onClickVHShow: (Ids) -> Unit,
    private var onClickVHPerson: (Ids) -> Unit
) : PagingDataAdapter<SearchResult, SearchViewholder>(SearchAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewholder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderSearchBinding.inflate(inflater, parent, false)
        return SearchViewholder(binding)
    }


    override fun onBindViewHolder(holder: SearchViewholder, position: Int) {
        val item = getItem(position) ?: return

        holder.bind(item)

        holder.itemView.setOnClickListener {
            when (item) {
                is SearchResult.MovieItem -> onClickVHMovie(item.movieBase.ids)
                is SearchResult.ShowItem -> onClickVHShow(item.showBase.ids)
                is SearchResult.PersonItem ->  onClickVHPerson(item.personBase.ids)
            }
        }
    }


    companion object : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return when {
                oldItem is SearchResult.MovieItem && newItem is SearchResult.MovieItem -> {
                    oldItem.movieBase.ids.trakt == newItem.movieBase.ids.trakt
                }

                oldItem is SearchResult.ShowItem && newItem is SearchResult.ShowItem -> {
                    oldItem.showBase.ids.trakt == newItem.showBase.ids.trakt
                }

                oldItem is SearchResult.PersonItem && newItem is SearchResult.PersonItem -> {
                    oldItem.personBase.ids.trakt == newItem.personBase.ids.trakt
                }

                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }
    }

}