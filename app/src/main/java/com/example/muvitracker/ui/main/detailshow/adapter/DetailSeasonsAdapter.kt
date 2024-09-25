package com.example.muvitracker.ui.main.detailmovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.ui.main.detailshow.adapter.SeasonVH

class DetailSeasonsAdapter(
    private val onClickVH: (Int) -> Unit,
    private val onClickWatchedAllCheckbox: (Int, () -> Unit) -> Unit, // callback stato loading
) : ListAdapter<SeasonExtended, SeasonVH>(DIFF_CALLBACK) {


    // ADAPTER METHODS
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = VhSeasonsOnDetailshowBinding.inflate(layoutInflater, parent, false)
        return SeasonVH(vhBinding)
    }

    override fun onBindViewHolder(holder: SeasonVH, position: Int) {
        val seasonItem = getItem(position)

        holder.bind(seasonItem)

        // click - apri season fragment
        holder.itemView.setOnClickListener {
            onClickVH(seasonItem.seasonNumber)
        }

        // stato default sempre iniziale per il nuovo elemento
        // per evitare animaz e stati element precedente
        holder.binding.seasonCheckbox.isEnabled = true
        holder.binding.watchedAllCheckboxLoadingBar.visibility = View.GONE


        // checkbox todo
        holder.binding.seasonCheckbox.setOnCheckedChangeListener(null)
        holder.binding.seasonCheckbox.isChecked =
            seasonItem.watchedAll // !! forma abbreviata - caso true
        // todo (eugi appunti) - listener viene chiamato alClick e al cambiamentoStatoChecked (quando lo stato cambia)

        holder.binding.seasonCheckbox.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                holder.binding.seasonCheckbox.isEnabled = false
                holder.binding.watchedAllCheckboxLoadingBar.visibility = View.VISIBLE

                onClickWatchedAllCheckbox.invoke(seasonItem.seasonNumber) {
                    holder.binding.watchedAllCheckboxLoadingBar.visibility = View.GONE
                    holder.binding.seasonCheckbox.isEnabled = true

                    holder.binding.seasonCheckbox.setOnCheckedChangeListener(null)
                    holder.binding.seasonCheckbox.isChecked = seasonItem.watchedAll
                    holder.binding.seasonCheckbox.setOnCheckedChangeListener(this)

                    holder.bind(seasonItem) // Re-bind per aggiornare i dati visualizzati

                }
            }
        })
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SeasonExtended>() {
            override fun areItemsTheSame(oldItem: SeasonExtended, newItem: SeasonExtended): Boolean {
                return oldItem.ids.trakt == newItem.ids.trakt
            }

            override fun areContentsTheSame(
                oldItem: SeasonExtended, newItem: SeasonExtended): Boolean {
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


