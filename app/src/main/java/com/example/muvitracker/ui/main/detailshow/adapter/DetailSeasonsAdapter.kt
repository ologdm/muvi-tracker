package com.example.muvitracker.ui.main.detailmovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.databinding.VhSeasonsOnDetailshowBinding
import com.example.muvitracker.ui.main.detailshow.adapter.SeasonVH

class DetailSeasonsAdapter(
    val onClickVH :(Int)->Unit,
) : ListAdapter<SeasonExtenDto, SeasonVH>(DIFF_CALLBACK) {


    // ADAPTER METHODS
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vhBinding = VhSeasonsOnDetailshowBinding.inflate(layoutInflater, parent, false)
        return SeasonVH(vhBinding)
    }

    override fun onBindViewHolder(holder: SeasonVH, position: Int) {
        val seasonDto = getItem(position)

        // uopdate view
        holder.bind(seasonDto)
        // CLICK - apri season fragment
        holder.itemView.setOnClickListener {
            onClickVH(seasonDto.number)
        }

        holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            // TODO - salvataggio stato intera stagione
        }

    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SeasonExtenDto>() {
            override fun areItemsTheSame(
                oldItem: SeasonExtenDto,
                newItem: SeasonExtenDto
            ): Boolean {
                return oldItem.ids.trakt == newItem.ids.trakt
            }

            override fun areContentsTheSame(
                oldItem: SeasonExtenDto,
                newItem: SeasonExtenDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


