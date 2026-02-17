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

class ProvidersAdapter : ListAdapter<Provider, ProviderViewholder>(diff) {
    // il padre richiede nel costruttore 'ItemCallback'


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
    }


    companion object {
        // va sempre nel companion, per una questione di istanziamento
        private val diff = object : DiffUtil.ItemCallback<Provider>() {
            override fun areItemsTheSame(oldItem: Provider, newItem: Provider): Boolean {
                return oldItem.providerId == newItem.providerId
            }

            override fun areContentsTheSame(oldItem: Provider, newItem: Provider): Boolean {
                return oldItem == newItem
            }
        }
    }

}


class ProviderViewholder(
    // da qua
    val b: ViewholderProviderBinding // e tutta la carcassa
) : RecyclerView.ViewHolder(
    // passo al costruttore padre
    b.root // itemview :View
) {

    fun bind(provider: Provider) {
        b.providerName.text = provider.providerName
        b.providingType.text = provider.serviceType

        Glide
            .with(b.root.context)
            .load(provider.logoPath)
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.providerIcon)
    }
}

