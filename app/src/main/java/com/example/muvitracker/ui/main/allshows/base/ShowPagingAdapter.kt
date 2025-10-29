package com.example.muvitracker.ui.main.allshows.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.ViewholderExploreBaseBinding
import com.example.muvitracker.domain.model.base.ShowBase


class ShowPagingAdapter(
    val onClickVH: (Ids) -> Unit
) : PagingDataAdapter<ShowBase, ShowViewholder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShowBase>() {
            override fun areItemsTheSame(oldItem: ShowBase, newItem: ShowBase): Boolean {
                return oldItem.ids.trakt == newItem.ids.trakt
            }

            override fun areContentsTheSame(oldItem: ShowBase, newItem: ShowBase): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = ViewholderExploreBaseBinding.inflate(layoutInflater, parent, false)
        return ShowViewholder(vhBinding)
    }


    override fun onBindViewHolder(holder: ShowViewholder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClickVH(item.ids)
            }
        }
    }

}




