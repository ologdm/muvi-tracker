package com.example.muvitracker.ui.main.detailmovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding
import com.example.muvitracker.ui.main.detailshow.adapter.SeasonVH

class DetailSeasonsAdapter(
    val onClickVH :(Int)->Unit,
//) : ListAdapter<SeasonExtenDto, SeasonVH>(DIFF_CALLBACK) {
) : ListAdapter<SeasonEntity, SeasonVH>(DIFF_CALLBACK) {


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

        // checkbox check
        if (seasonItem.watchedAll){
            holder.binding.checkbox.isChecked =true
        }else{
            holder.binding.checkbox.isChecked =false
        }


    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SeasonEntity>() {
            override fun areItemsTheSame(
                oldItem: SeasonEntity,
                newItem: SeasonEntity
            ): Boolean {
                return oldItem.seasonTraktId == newItem.seasonTraktId
            }

            override fun areContentsTheSame(
                oldItem: SeasonEntity,
                newItem: SeasonEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


