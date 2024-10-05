package com.example.muvitracker.ui.main.detailmovie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.VhRelatedListOnDetailBinding
import com.example.muvitracker.domain.model.base.Movie

class RelatedMovieAdapter(
    private val onClickVH: (Ids) -> Unit,
) : ListAdapter<Movie, RelatedMovieVH>(RelatedMovieAdapter) {


    companion object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedMovieVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhRelatedListOnDetailBinding.inflate(layoutInflater, parent, false)
        return RelatedMovieVH(bindingVh)
    }

    override fun onBindViewHolder(holder: RelatedMovieVH, position: Int) {
        val item = getItem(position)

        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickVH(item.ids)
        }
    }


}