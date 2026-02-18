package com.example.muvitracker.ui.main.detailmovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.databinding.ViewholderProviderBinding
import com.example.muvitracker.domain.model.Provider

// TODO sia x movie che show
class ProvidersAdapter(
    val onClickVH: (Provider) -> Unit
) : ListAdapter<Provider, ProviderViewholder>(diff) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProviderViewholder {
        val inflater = LayoutInflater.from(parent.context)
        val bindingVh = ViewholderProviderBinding.inflate(inflater, parent, false)
        return ProviderViewholder(bindingVh)
    }

    override fun onBindViewHolder(
        holder: ProviderViewholder,
        position: Int
    ) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickVH.invoke(item)
        }
    }


    companion object {
        private val diff = object : DiffUtil.ItemCallback<Provider>() {
            override fun areItemsTheSame(oldItem: Provider, newItem: Provider): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Provider, newItem: Provider): Boolean {
                return oldItem == newItem
            }
        }
    }
}


class ProviderViewholder(
    val b: ViewholderProviderBinding
) : RecyclerView.ViewHolder(
    b.root
) {

    fun bind(provider: Provider) {
        b.providerName.text = provider.name
        b.providingType.text = provider.serviceType

        Glide.with(b.root.context)
            .load(provider.logoUrl)
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.providerIcon)
    }
}

