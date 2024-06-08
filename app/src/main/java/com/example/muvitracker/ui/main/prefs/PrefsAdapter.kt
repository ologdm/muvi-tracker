package com.example.muvitracker.ui.main.prefs

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhPrefsBinding
import com.example.muvitracker.domain.model.DetailMovie


class PrefsAdapter(
    private val onClickVH: (movieId: Int) -> Unit,
    private val onCLickLiked: (DetailMovie) -> Unit,
    private val onClickWatched: (DetailMovie, Boolean) -> Unit
) : ListAdapter<DetailMovie, PrefsVH>(PrefsAdapter) {

    companion object : DiffUtil.ItemCallback<DetailMovie>() {
        override fun areItemsTheSame(oldItem: DetailMovie, newItem: DetailMovie): Boolean {
            return oldItem.ids.trakt == oldItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: DetailMovie, newItem: DetailMovie): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefsVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VhPrefsBinding.inflate(layoutInflater, parent, false)
        return PrefsVH(binding)
    }


    override fun onBindViewHolder(holder: PrefsVH, position: Int) {
        var currentItem = getItem(position)

        val context: Context = holder.itemView.context
        val iconFilled = context.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context.getDrawable(R.drawable.baseline_liked_border)

        holder.bind(currentItem) // update views

        holder.binding.run {

            holder.itemView.setOnClickListener {
                onClickVH.invoke(currentItem.ids.trakt)
            }

            likedButton.setOnClickListener {
                onCLickLiked.invoke(currentItem)
                updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)
            }

            watchedCheckBox.setOnCheckedChangeListener(null)
            watchedCheckBox.isChecked = currentItem.watched
            watchedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onClickWatched.invoke(currentItem, isChecked)
            }

            updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)
        }

    }


    fun updateFavoriteIcon(
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
}

