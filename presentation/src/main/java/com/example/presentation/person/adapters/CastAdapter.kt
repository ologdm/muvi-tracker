package com.example.presentation.person.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.CastMember
import com.example.domain.model.Ids
import com.example.presentation.databinding.ViewholderCastListOnDetailBinding


class CastAdapter(
    private val onClickVH: (Ids, String) -> Unit,
) : androidx.recyclerview.widget.ListAdapter<CastMember, CastViewholder>(CastAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = ViewholderCastListOnDetailBinding.inflate(layoutInflater, parent, false)
        return CastViewholder(bindingVh)
    }

    override fun onBindViewHolder(holder: CastViewholder, position: Int) {

        val item = getItem(position)

        holder.bind(item)

        holder.itemView.setOnClickListener {
            item?.let { it ->
                onClickVH.invoke(it.personBase.ids, it.character)
            }
        }

    }


    companion object : DiffUtil.ItemCallback<CastMember>() {
        override fun areItemsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem.personBase.ids.trakt == newItem.personBase.ids.trakt
        }

        override fun areContentsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem == newItem
        }
    }


}