package com.example.muvitracker.inkotlin.mainactivity.base.popu

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhPopuBinding
import com.example.muvitracker.inkotlin.repo.dto.base.PopuDto


// kotlin
// 1) conversione automatica Int ==> annoVH.setText(dto.year.toString()) ==> //
// 2)


class PopuAdapter : RecyclerView.Adapter<PopuVh>() {

    // ATTRIBUTI
    // variante 1 val/mutablelist
    private val adapterList = mutableListOf<PopuDto>()

    /* variante 2 boxo
     * private var adapterList = listOf<PopularDtoK>()
     */

    private var callbackVH: ((Int) -> Unit)? = null


    // METODI
    // 1. OK
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopuVh {
        // definisco il mio binding
        val binding = VhPopuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopuVh(binding)
    }


    // 2. OK
    override fun getItemCount(): Int {
        return adapterList.size
    }


    // 3. OK
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PopuVh, position: Int) {

        val dto: PopuDto = adapterList[position]

        with(holder.binding) {
            //titleVH.text = "${dto.title} (${dto.year.toString()})"
            // titleVH.text = getString(R.string.title_with_year, dto.title, dto.year)
            titleVH.text = "${dto.title} (${dto.year})"

            /*
            Glide.with(root.context)
                .load(dto.getImageUrl())
                .into(imageVH)

             */

            Glide.with(root.context)
                .load(dto.getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.glide_placeholder_test)
                .error(R.drawable.glide_error_test)
                .into(imageVH)


            root.setOnClickListener { // callback view - implementazione, chiamata al click
                callbackVH?.invoke(dto.ids.trakt) // callbackVH - chiamata, implementazione su fragment
            }
        }
    }


    // ALTRI METODI

    // OK
    fun updateList(inputList: List<PopuDto>) {
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









