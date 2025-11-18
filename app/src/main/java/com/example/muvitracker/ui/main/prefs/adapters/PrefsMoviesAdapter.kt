package com.example.muvitracker.ui.main.prefs.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.R
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.ViewholderPrefsMovieBinding
import com.example.muvitracker.domain.model.Movie


class PrefsMoviesAdapter(
    private val onClickVH: (movieIds: Ids) -> Unit,
    private val onLongClickVH: (movieId: Int) -> Unit,
    private val onCLickLiked: (Int) -> Unit,
    private val onClickWatched: (Movie, Boolean) -> Unit,
    private val onNotLikedNotWatched: (movie: Movie) -> Unit
) : ListAdapter<Movie, PrefsMovieViewholder>(PrefsMoviesAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefsMovieViewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewholderPrefsMovieBinding.inflate(layoutInflater, parent, false)
        return PrefsMovieViewholder(binding)
    }


    override fun onBindViewHolder(holder: PrefsMovieViewholder, position: Int) {
        var currentItem = getItem(position)

        val context: Context = holder.itemView.context
        val iconFilled = context.getDrawable(R.drawable.liked_icon_filled)
        val iconEmpty = context.getDrawable(R.drawable.liked_icon_empty)

        holder.bind(currentItem) // update views

        holder.binding.run {

            updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)

            // --- LIKE BUTTON ------------------------------------------------------------------------
            likedButton.setOnClickListener {
                // 1. Stato ipotetico dopo il toggle del like
                val newLikedState = !currentItem.liked

                // 2. se il toggle porterebbe a entrambi disattivati -> Discard Dialog
                if (!newLikedState && !currentItem.watched) {
                    onNotLikedNotWatched.invoke(currentItem)
                    return@setOnClickListener
                }

                // toggle reale
                onCLickLiked.invoke(currentItem.ids.trakt)
                updateFavoriteIcon(likedButton, currentItem.liked, iconFilled, iconEmpty)
            }


            // --- WATCHED CHECKBOX ------------------------------------------------------------------
            // checkbox update
            watchedCheckBox.setOnCheckedChangeListener(null)
            watchedCheckBox.isChecked = currentItem.watched

            // 3. listener
//            watchedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            watchedCheckBox.setOnCheckedChangeListener (object :
            CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

                    // 1. proponi Discard quando liked e watched disabilitati ed esci
                    val newWatchedState = isChecked

                    if (!currentItem.liked && !newWatchedState) {
                        onNotLikedNotWatched.invoke(currentItem)

                        // ripristina lo stato precedente
                        watchedCheckBox.setOnCheckedChangeListener(null)
                        watchedCheckBox.isChecked = currentItem.watched
                        watchedCheckBox.setOnCheckedChangeListener(this)

                        return@onCheckedChanged // -> esci dalla funzione senza eseguire il codice sottostante
                    }

                    // 2.2 // toggle reale
                    onClickWatched.invoke(currentItem, isChecked)
                }
            })

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


    companion object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}

