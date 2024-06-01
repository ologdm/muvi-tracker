package com.example.muvitracker.inkotlin.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.inkotlin.data.dto.SearchDto


class SearchAdapter(
    private var onClickCallback: (Int) -> Unit,
) : ListAdapter<SearchDto, SearchVh>(SearchAdapter) {


    companion object : DiffUtil.ItemCallback<SearchDto>() {
        override fun areItemsTheSame(oldItem: SearchDto, newItem: SearchDto): Boolean {
            return oldItem.movie?.ids?.trakt ==
                    newItem.movie?.ids?.trakt
        }

        override fun areContentsTheSame(oldItem: SearchDto, newItem: SearchDto): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVh {
        val binding = VhSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchVh(binding)
    }


    override fun onBindViewHolder(holder: SearchVh, position: Int) {
        val item = getItem(position)

        holder.bind(item) // update on VH

        holder.itemView.setOnClickListener {
            onClickCallback.invoke(item.movie?.ids!!.trakt) // TODO check nullable
        }
    }

}