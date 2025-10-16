package com.example.muvitracker.ui.main.detailshow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.databinding.VhRelatedListOnDetailBinding
import com.example.muvitracker.domain.model.base.Show

// vh solo immagine senza titolo

class RelatedShowsAdapter(
    private val onClickVH: (Ids) -> Unit,
) : ListAdapter<Show, RelatedShowVH>(RelatedShowsAdapter) {

    companion object : DiffUtil.ItemCallback<Show>() {
        override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedShowVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhRelatedListOnDetailBinding.inflate(layoutInflater, parent, false)
        println("YYY related create vh")
        return RelatedShowVH(bindingVh)
    }

    override fun onBindViewHolder(holder: RelatedShowVH, position: Int) {
        val show = getItem(position)

        holder.bind(show)

        holder.itemView.setOnClickListener {
            onClickVH(show.ids)
        }
    }


}