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
import com.example.muvitracker.ui.main.detailshow.adapter.SeasonVH

class DetailSeasonsAdapter(
    private val onClickVH: (Int) -> Unit,
    private val onClickWatchedAllCheckbox: (Int, () -> Unit) -> Unit, // callback stato loading
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


        // checkbox todo
        holder.binding.seasonCheckbox.setOnCheckedChangeListener(null)
        holder.binding.seasonCheckbox.isChecked =
            seasonItem.watchedAll // !! forma abbreviata - caso true
        // todo (eugi appunti) - listener viene chiamato alClick e al cambiamentoStatoChecked (quando lo stato cambia)

        holder.binding.seasonCheckbox.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                // 1 checkbox disatt + progressbar ativa
                holder.binding.seasonCheckbox.isEnabled = false
                holder.binding.watchedAllCheckboxLoadingBar.visibility = View.VISIBLE

                // 2 callback
                // e invocata quando la chiamata assincrona di viewmodel finisce
                onClickWatchedAllCheckbox.invoke(seasonItem.seasonNumber) {
                    // Dopo che l'operazione è terminata
                    holder.binding.watchedAllCheckboxLoadingBar.visibility = View.GONE
                    holder.binding.seasonCheckbox.isEnabled = true

                    // 1 per non chiamare listener al cambiamento checkbox -> // tolgo listener- (null)
                    holder.binding.seasonCheckbox.setOnCheckedChangeListener(null)
                    holder.binding.seasonCheckbox.isChecked = seasonItem.watchedAll
                    // rimetto listener com'esta prima (this)
                    holder.binding.seasonCheckbox.setOnCheckedChangeListener(this)
                    // this si puo usare solo con object e non lambda (lambda perde il contesto della classe anonima)

                    holder.bind(seasonItem) // Re-bind per aggiornare i dati visualizzati

                }
            }
        })
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SeasonEntity>() {
            override fun areItemsTheSame(oldItem: SeasonEntity, newItem: SeasonEntity): Boolean {
                return oldItem.seasonTraktId == newItem.seasonTraktId
            }

            override fun areContentsTheSame(oldItem: SeasonEntity, newItem: SeasonEntity): Boolean {
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


