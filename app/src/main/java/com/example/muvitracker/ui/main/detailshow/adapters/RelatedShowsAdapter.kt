package com.example.muvitracker.ui.main.detailshow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.ViewholderRelatedListOnDetailBinding
import com.example.muvitracker.domain.model.base.ShowBase

// vh solo immagine senza titolo

class RelatedShowsAdapter(
    private val onClickVH: (Ids) -> Unit,
) : ListAdapter<ShowBase, RelatedShowViewholder>(RelatedShowsAdapter) {

    companion object : DiffUtil.ItemCallback<ShowBase>() {
        override fun areItemsTheSame(oldItem: ShowBase, newItem: ShowBase): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: ShowBase, newItem: ShowBase): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedShowViewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = ViewholderRelatedListOnDetailBinding.inflate(layoutInflater, parent, false)
        println("YYY related create vh")
        return RelatedShowViewholder(bindingVh)
    }

    override fun onBindViewHolder(holder: RelatedShowViewholder, position: Int) {
        val show = getItem(position)

        holder.bind(show)

        holder.itemView.setOnClickListener {
            onClickVH(show.ids)
        }
    }


}