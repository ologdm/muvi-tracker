package com.example.muvitracker.ui.main.detailmovie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.dto.xperson.PersonExtendedDto
import com.example.muvitracker.databinding.VhRelatedListOnDetailBinding
import com.example.muvitracker.domain.model.base.Movie

class CastAdapter(
    private val onClickVH: (Ids) -> Unit,
) : ListAdapter<PersonExtendedDto, CastPersonVH>(CastAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastPersonVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhRelatedListOnDetailBinding.inflate(layoutInflater, parent, false)
        return CastPersonVH(bindingVh)
    }

    override fun onBindViewHolder(holder: CastPersonVH, position: Int) {
        TODO("Not yet implemented")
    }




    companion object : DiffUtil.ItemCallback<PersonExtendedDto>() {
        override fun areItemsTheSame(oldItem: PersonExtendedDto, newItem: PersonExtendedDto): Boolean {
            return oldItem.ids.trakt == newItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: PersonExtendedDto, newItem: PersonExtendedDto): Boolean {
            return oldItem == newItem
        }
    }


}