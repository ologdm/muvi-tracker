package com.example.muvitracker.ui.main.detailmovie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.dto.xperson.CastMember
import com.example.muvitracker.databinding.VhCastListOnDetailBinding

class CastAdapter(
    private val onClickVH: (Ids) -> Unit,
) : ListAdapter<CastMember, CastVH>(CastAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingVh = VhCastListOnDetailBinding.inflate(layoutInflater, parent, false)
        return CastVH(bindingVh)
    }

    override fun onBindViewHolder(holder: CastVH, position: Int) {
//        TODO("Not yet implemented")

        val item = getItem(position)

        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickVH.invoke(item.person!!.ids)
        }


    }


    companion object : DiffUtil.ItemCallback<CastMember>() {
        override fun areItemsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem.person?.ids?.trakt == newItem.person?.ids?.trakt
        }

        override fun areContentsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem == newItem
        }
    }


}