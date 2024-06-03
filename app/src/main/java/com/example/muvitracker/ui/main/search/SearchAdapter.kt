package com.example.muvitracker.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.data.dto.SearchDto


class SearchAdapter(
    private var onClickCallback: (Int) -> Unit,
) : ListAdapter<SearchDto, SearchVH>(SearchAdapter) {


    companion object : DiffUtil.ItemCallback<SearchDto>() {
        override fun areItemsTheSame(oldItem: SearchDto, newItem: SearchDto): Boolean {
            return oldItem.movie?.ids?.trakt ==
                    newItem.movie?.ids?.trakt
        }

        override fun areContentsTheSame(oldItem: SearchDto, newItem: SearchDto): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val binding = VhSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchVH(binding)
    }


    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        val item = getItem(position)

        holder.bind(item) // update on VH

        holder.itemView.setOnClickListener {
            onClickCallback.invoke(item.movie?.ids!!.trakt) // TODO check nullable
        }
    }

}