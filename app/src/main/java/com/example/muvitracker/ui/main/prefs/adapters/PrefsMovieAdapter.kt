package com.example.muvitracker.ui.main.prefs.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.VhPrefsMovieBinding
import com.example.muvitracker.domain.model.DetailMovie


class PrefsMovieAdapter(
    private val onClickVH: (movieIds: Ids) -> Unit,
    private val onLongClickVH: (movieId: Int) -> Unit,
    private val onCLickLiked: (Int) -> Unit,
    private val onClickWatched: (DetailMovie, Boolean) -> Unit
) : ListAdapter<DetailMovie, PrefsMovieVH>(PrefsMovieAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefsMovieVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VhPrefsMovieBinding.inflate(layoutInflater, parent, false)
        return PrefsMovieVH(binding)
    }


    override fun onBindViewHolder(holder: PrefsMovieVH, position: Int) {
        var currentItem = getItem(position)

        val context: Context = holder.itemView.context
        val iconFilled = context.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context.getDrawable(R.drawable.baseline_liked_border)

        holder.bind(currentItem) // update views

        holder.binding.run {

            updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)

            likedButton.setOnClickListener {
                onCLickLiked.invoke(currentItem.ids.trakt)
                updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)
            }

            watchedCheckBox.setOnCheckedChangeListener(null)
            watchedCheckBox.isChecked = currentItem.watched
            watchedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onClickWatched.invoke(currentItem, isChecked)
            }

            holder.itemView.setOnClickListener {
                onClickVH.invoke(currentItem.ids)
            }

            holder.itemView.setOnLongClickListener {
                onLongClickVH.invoke(currentItem.ids.trakt)
                true
            }
        }
    }

    // ################################################################################

    private fun updateFavoriteIcon(
        likedButton: ImageButton,
        isLiked: Boolean,
        iconFilled: Drawable?,
        iconEmpty: Drawable?
    ) {
        if (isLiked)
            likedButton.setImageDrawable(iconFilled)
        else
            likedButton.setImageDrawable(iconEmpty)
    }


    companion object : DiffUtil.ItemCallback<DetailMovie>() {
        override fun areItemsTheSame(oldItem: DetailMovie, newItem: DetailMovie): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: DetailMovie, newItem: DetailMovie): Boolean {
            return oldItem == newItem
        }
    }
}

