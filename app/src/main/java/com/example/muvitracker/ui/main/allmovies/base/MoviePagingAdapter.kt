package com.example.muvitracker.ui.main.allmovies.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.VhBaseListBinding
import com.example.muvitracker.domain.model.base.MovieBase

// used by:
// - popular fragment - lists with paging needed


class MoviePagingAdapter (
    val onClickVH: (Ids) -> Unit
) : PagingDataAdapter<MovieBase, MovieVH>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieBase>() {
            override fun areItemsTheSame(oldItem: MovieBase, newItem: MovieBase): Boolean {
                return oldItem.ids.trakt == newItem.ids.trakt
            }

            override fun areContentsTheSame(oldItem: MovieBase, newItem: MovieBase): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhBaseListBinding.inflate(layoutInflater, parent, false)
        return MovieVH(bindingVh)
    }


    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClickVH(item.ids)
            }
        }
    }

}




