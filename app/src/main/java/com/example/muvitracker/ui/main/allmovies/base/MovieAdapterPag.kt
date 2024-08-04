package com.example.muvitracker.ui.main.allmovies.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.muvitracker.databinding.VhMovieBinding
import com.example.muvitracker.domain.model.base.Movie


class MovieAdapterPag(
    private val onClickVH: (Int) -> Unit,
) : PagingDataAdapter<Movie, MovieVh>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.ids.trakt == newItem.ids.trakt
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem // se dataclass -->  ==  uguale a .equals()
            }

        }
    }


    // gli dico come creare i viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVh {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhMovieBinding.inflate(layoutInflater, parent, false)
        return MovieVh(bindingVh)
    }

    // gli dico come gestire ogni Vh creato
    override fun onBindViewHolder(holder: MovieVh, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClickVH(item.ids.trakt)
            }
        }
    }

}




