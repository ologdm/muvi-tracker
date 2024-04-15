package com.example.muvitracker.inkotlin.mainactivity.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhBaseBinding


// kotlin
// 1) conversione automatica Int ==> annoVH.setText(dto.year.toString()) ==> //
// 2)


/**
//  <BaseVh>
//  MovieModel
// si utilizzerà su PopuFragment e BoxoFragment
*/


class BaseAdapter : RecyclerView.Adapter<BaseVh>() {

    // ATTRIBUTI
    // variante 1 val/mutablelist // TODO OK
    private val adapterList = mutableListOf<MovieModel>()

    /* variante 2 boxo
     * private var adapterList = listOf<PopularDtoK>()
     */

    private var callbackVH: ((Int) -> Unit)? = null


    // METODI
    // 1. OK
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVh {
        // definisco il mio binding
        val binding = VhBaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BaseVh(binding)
    }


    // 2. OK
    override fun getItemCount(): Int {
        return adapterList.size
    }


    // 3. OK
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseVh, position: Int) {

        // TODO  OK
        val dto: MovieModel = adapterList[position]

        with(holder.binding) {
            //titleVH.text = "${dto.title} (${dto.year.toString()})"
            // titleVH.text = getString(R.string.title_with_year, dto.title, dto.year)
            titleVH.text = "${dto.title} (${dto.year})"


            Glide.with(root.context)
                .load(dto.getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.glide_placeholder)
                .error(R.drawable.glide_error)
                .into(imageVH)


            root.setOnClickListener { // callback view - implementazione, chiamata al click
                callbackVH?.invoke(dto.ids.trakt) // callbackVH - chiamata, implementazione su fragment
            }
        }
    }


    // ALTRI METODI

    // OK TODO
    fun updateList(inputList: List<MovieModel>) {
        // 1 variante - val mutable
        adapterList.clear() // elimina elementi lista mutabile
        adapterList.addAll(inputList) // aggiungi elementi alla lista
        // senza clear, aggiungi elementi a quelli esistenti

        notifyDataSetChanged()

        //2 variante - var list
        //adapterList = inputList.toList() // sotituisco lista con copia lista passata
    }


    // OK
    fun setCallbackVH(call: (Int) -> Unit) {
        this.callbackVH = call
    }

}







