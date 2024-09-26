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
import com.example.muvitracker.databinding.VhPrefsShowBinding
import com.example.muvitracker.domain.model.DetailShow


class PrefsShowAdapter(
    private val onClickVH: (movieIds: Ids) -> Unit,
    private val onLongClickVH: (movieId: Int) -> Unit,
    private val onCLickLiked: (Int) -> Unit,
    private val onClickWatched: (DetailShow, Boolean) -> Unit
) : ListAdapter<DetailShow, PrefsShowVH>(PrefsShowAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefsShowVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VhPrefsShowBinding.inflate(layoutInflater, parent, false)
        return PrefsShowVH(binding)
    }


    override fun onBindViewHolder(holder: PrefsShowVH, position: Int) {
        var currentItem = getItem(position)

        val context: Context = holder.itemView.context
        val iconFilled = context.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context.getDrawable(R.drawable.baseline_liked_border)


        holder.bind(currentItem) // update views

        holder.binding.run {

            // liked update + toggle
            updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)

            likedButton.setOnClickListener {
                onCLickLiked.invoke(currentItem.ids.trakt)
                updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)
            }

            // watched update + toggle TODO
            watchedCheckBox.isChecked =
                currentItem.watchedAll // !! forma abbreviata - caso true

            watchedCounterProgressBar.max = currentItem.airedEpisodes
            watchedCounterProgressBar.progress = currentItem.watchedCount
            watchedCounterTextview.text = "${currentItem.watchedCount}/${currentItem.airedEpisodes}"


            // todo alla fine
            // pass watchedState
            watchedCheckBox.setOnCheckedChangeListener(null)
            watchedCheckBox.isChecked = currentItem.watchedAll
            watchedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onClickWatched.invoke(currentItem, isChecked)
            }

            // open detail
            holder.itemView.setOnClickListener {
                onClickVH.invoke(currentItem.ids)
            }

            // delete item
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


    companion object : DiffUtil.ItemCallback<DetailShow>() {
        override fun areItemsTheSame(oldItem: DetailShow, newItem: DetailShow): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: DetailShow, newItem: DetailShow): Boolean {
            return oldItem == newItem
        }
    }
}

