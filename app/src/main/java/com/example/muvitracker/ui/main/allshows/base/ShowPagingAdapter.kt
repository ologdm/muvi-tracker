package com.example.muvitracker.ui.main.allshows.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.VhBaseListBinding
import com.example.muvitracker.domain.model.base.ShowBase


class ShowPagingAdapter(
    val onClickVH: (Ids) -> Unit
) : PagingDataAdapter<ShowBase, ShowVH>(DIFF_CALLBACK) {

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = VhBaseListBinding.inflate(layoutInflater, parent, false)
        return ShowVH(vhBinding)
    }


    override fun onBindViewHolder(holder: ShowVH, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClickVH(item.ids)
            }
        }
    }

}




