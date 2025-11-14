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
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.ViewholderPrefsShowBinding
import com.example.muvitracker.domain.model.Show


class PrefsShowsAdapter(
    private val onClickVH: (movieIds: Ids) -> Unit,
    private val onLongClickVH: (movieId: Int) -> Unit,
    private val onCLickLiked: (Int) -> Unit,
    private val onClickWatchedAllCheckbox: (showIds: Ids, () -> Unit) -> Unit,
    private val onNotLikedNotWatched: (show: Show) -> Unit
) : ListAdapter<Show, PrefsShowViewholder>(PrefsShowsAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefsShowViewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewholderPrefsShowBinding.inflate(layoutInflater, parent, false)
        return PrefsShowViewholder(binding)
    }


    override fun onBindViewHolder(holder: PrefsShowViewholder, position: Int) {
        var currentItem = getItem(position)

        val context: Context = holder.itemView.context
        val iconFilled = context.getDrawable(R.drawable.liked_icon_filled)
        val iconEmpty = context.getDrawable(R.drawable.liked_icon_empty)


        holder.bind(currentItem) // update views

        holder.binding.run {

            // liked update + toggle
            updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)

            // --- LIKE BUTTON ------------------------------------------------------------------------
            likedButton.setOnClickListener {
                // 1. Stato ipotetico dopo il toggle del like
                val newLikedState = !currentItem.liked

                // 2. se il toggle porterebbe a entrambi disattivati -> Discard Dialog
                if (!newLikedState && !currentItem.watchedAll) {
                    onNotLikedNotWatched.invoke(currentItem)
                    return@setOnClickListener
                }

                // toggle reale
                onCLickLiked.invoke(currentItem.ids.trakt)
                updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)
            }


            // --- WATCHED ALL CHECKBOX --------------------------------------------------------------------
            // 1. stato default - per pulizia da precedenti modifiche
            watchedAllCheckBox.isEnabled =
                currentItem.airedEpisodes > 0 // disabilitazione se no aired episodes
            holder.binding.watchedAllCheckboxLoadingBar.visibility = View.GONE
            // 2. checkbox update
            watchedAllCheckBox.setOnCheckedChangeListener(null)
            watchedAllCheckBox.isChecked = currentItem.watchedAll
            // 3. listener
            watchedAllCheckBox.setOnCheckedChangeListener(object :
                CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

                    // 1. proponi Discard quando liked e watched disabilitati ed esci
                    val newWatchedState = isChecked
                    val wouldBeUnlikedAndUnwatched = (!currentItem.liked && !newWatchedState)
                    if (wouldBeUnlikedAndUnwatched) {
                        onNotLikedNotWatched.invoke(currentItem)

                        // ripristina lo stato precedente
                        watchedAllCheckBox.setOnCheckedChangeListener(null)
                        watchedAllCheckBox.isChecked = currentItem.watchedAll
                        watchedAllCheckBox.setOnCheckedChangeListener(this)

                        return@onCheckedChanged // -> esci dalla funzione senza eseguire il codice sottostante
                    }

                    // 2.1  loading UI
                    watchedAllCheckBox.isEnabled = false
                    watchedAllCheckboxLoadingBar.visibility = View.VISIBLE

                    // 2.2 // toggle reale
                    onClickWatchedAllCheckbox.invoke(currentItem.ids) {
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

    // --------------------------------------------------------------------------------------------------
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


    companion object : DiffUtil.ItemCallback<Show>() {
        override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem == newItem
        }
    }
}

