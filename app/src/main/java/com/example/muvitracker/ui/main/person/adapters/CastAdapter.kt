package com.example.muvitracker.ui.main.person.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.databinding.VhCastListOnDetailBinding
import com.example.muvitracker.domain.model.CastMember

// TODO OK domain
class CastAdapter(
    private val onClickVH: (Ids, String) -> Unit,
) : ListAdapter<CastMember, CastVH>(CastAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhCastListOnDetailBinding.inflate(layoutInflater, parent, false)
        return CastVH(bindingVh)
    }

    override fun onBindViewHolder(holder: CastVH, position: Int) {

        val item = getItem(position)

        holder.bind(item)

        holder.itemView.setOnClickListener {
            item?.let { it ->
                onClickVH.invoke(it.personBase.ids, it.character)
            }
        }


    }

    // TODO OK domain
    companion object : DiffUtil.ItemCallback<CastMember>() {
        override fun areItemsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem.personBase.ids.trakt == newItem.personBase.ids.trakt
        }

        override fun areContentsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem == newItem
        }
    }


}