package com.example.muvitracker.ui.main.prefs.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.databinding.VhPrefsShowBinding
import com.example.muvitracker.domain.model.DetailShow


class PrefsShowAdapter(
    private val onClickVH: (movieIds: Ids) -> Unit,
    private val onLongClickVH: (movieId: Int) -> Unit,
    private val onCLickLiked: (Int) -> Unit,
    private val onClickWatchedAllCheckbox: (showId: Int, () -> Unit) -> Unit
) : ListAdapter<DetailShow, PrefsShowVH>(PrefsShowAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefsShowVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VhPrefsShowBinding.inflate(layoutInflater, parent, false)
        return PrefsShowVH(binding)
    }


    override fun onBindViewHolder(holder: PrefsShowVH, position: Int) {
        var currentItem = getItem(position)

        val context: Context = holder.itemView.context
        val iconFilled = context.getDrawable(R.drawable.liked_icon_filled)
        val iconEmpty = context.getDrawable(R.drawable.liked_icon_empty)


        holder.bind(currentItem) // update views

        holder.binding.run {

            // liked update + toggle
            updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)

            likedButton.setOnClickListener {
                onCLickLiked.invoke(currentItem.ids.trakt)
                updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)
            }

            // stato default - per pulizia da precedenti modifiche
            watchedAllCheckBox.isEnabled = true
            holder.binding.watchedAllCheckboxLoadingBar.visibility = View.GONE

            // checkbox update -
            watchedAllCheckBox.setOnCheckedChangeListener(null)
            watchedAllCheckBox.isChecked = currentItem.watchedAll

            // UPDATE SDK 34 to 36
            watchedAllCheckBox.setOnCheckedChangeListener(object :
                CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                    watchedAllCheckBox.isEnabled = false
                    watchedAllCheckboxLoadingBar.visibility = View.VISIBLE

                    onClickWatchedAllCheckbox.invoke(currentItem.ids.trakt) {
                        watchedAllCheckBox.isEnabled = true
                        watchedAllCheckboxLoadingBar.visibility = View.GONE

                        // aggiorno di nuovo chekbox
                        watchedAllCheckBox.setOnCheckedChangeListener(null)
                        watchedAllCheckBox.isChecked = currentItem.watchedAll
                        watchedAllCheckBox.setOnCheckedChangeListener(this)

                        holder.bind(currentItem)
                    }
                }
            })
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

