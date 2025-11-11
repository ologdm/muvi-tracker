package com.example.muvitracker.ui.main.detailmovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.databinding.ViewholderSeasonsOnDetailshowBinding
import com.example.muvitracker.domain.model.Season
import com.example.muvitracker.ui.main.detailshow.adapters.SeasonViewholder

class DetailSeasonsAdapter(
    private val onClickVH: (Int) -> Unit,
    private val onClickWatchedAllCheckbox: (Int, () -> Unit) -> Unit, // callback stato loading
) : ListAdapter<Season, SeasonViewholder>(DIFF_CALLBACK) {


    // ADAPTER METHODS
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = ViewholderSeasonsOnDetailshowBinding.inflate(layoutInflater, parent, false)
        return SeasonViewholder(vhBinding)
    }

    override fun onBindViewHolder(holder: SeasonViewholder, position: Int) {
        val seasonItem = getItem(position)

        val context = holder.b.root.context
        holder.bind(seasonItem, context)

        // click - apri season fragment
        holder.itemView.setOnClickListener {
            onClickVH(seasonItem.seasonNumber)
        }

        // WATCHED CHECKBOX
        // stato default sempre iniziale per il nuovo elemento (per evitare animaz e stati elementi precedenti)
        holder.b.seasonCheckbox.isEnabled = seasonItem.airedEpisodes != 0

        holder.b.watchedAllCheckboxLoadingBar.visibility = View.GONE

        // sempre set(null) -> sempre prima di update -> aggiornamento checkbox senza trigger del listener
        holder.b.seasonCheckbox.setOnCheckedChangeListener(null)
        holder.b.seasonCheckbox.isChecked = seasonItem.watchedAll

        // UPDATE SDK 34 to 36
        holder.b.seasonCheckbox.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                holder.b.seasonCheckbox.isEnabled = false
                holder.b.watchedAllCheckboxLoadingBar.visibility = View.VISIBLE

                // ad operazione loading conclusa, la viewmodel callback chiama questa callabck
                onClickWatchedAllCheckbox.invoke(seasonItem.seasonNumber) {
                    holder.b.watchedAllCheckboxLoadingBar.visibility = View.GONE
                    holder.b.seasonCheckbox.isEnabled = true
                    // aggiorno di nuovo chekbox
                    holder.b.seasonCheckbox.setOnCheckedChangeListener(null)
                    holder.b.seasonCheckbox.isChecked = seasonItem.watchedAll
                    holder.b.seasonCheckbox.setOnCheckedChangeListener(this)

                    holder.bind(seasonItem, context) // Re-bind per aggiornare i dati visualizzati
                }
            }
        })
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Season>() {
            override fun areItemsTheSame(
                oldItem: Season,
                newItem: Season
            ): Boolean {
                return oldItem.ids.trakt == newItem.ids.trakt
            }

            override fun areContentsTheSame(
                oldItem: Season, newItem: Season
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


// senza loading
//        holder.binding.seasonCheckbox.setOnCheckedChangeListener { _, isChecked ->
//            onClickWatchedAllCheckbox.invoke(seasonItem.seasonNumber) { success ->
//
//
//                holder.bind(seasonItem)
//            }


