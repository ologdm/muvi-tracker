package com.example.muvitracker.mainactivity.popular

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.repository.dto.PopularDtoK
import com.example.muvitracker.utils.CallbackVH

// kotlin
// 1) conversione automatica Int ==> annoVH.setText(dto.year.toString()) ==> //
// 2) updateList ==> adapterList = inputList.toList().toMutableList() ??? eugi
// 3)
// 4)


class PopularAdapterK : RecyclerView.Adapter<PopularVHK>() {

    // lista OK
    // elementi singoli mutabili
    // riferimento in memoria lista immutabile
    // TODO: modificare in val
    val adapterList = mutableListOf<PopularDtoK>()


    // 1. OK
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularVHK {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.vh_popular_grid, parent, false)
        return PopularVHK(view)
    }

    // 2. OK
    override fun getItemCount(): Int {
        return adapterList.size
    }

    // 3. OK
    override fun onBindViewHolder(holder: PopularVHK, position: Int) {

        // 1 prendi elemento da mostrare OK
        val dto: PopularDtoK = adapterList.get(position)

        // 2 identifica views OK
        val titoloVH: TextView = holder.itemView.findViewById(R.id.titoloVH)
        val annoVH: TextView = holder.itemView.findViewById(R.id.annoVH)
        val imageVH: ImageView = holder.itemView.findViewById(R.id.imageVH)

        // 3 set testo OK
        titoloVH.setText(dto.title)
        annoVH.setText(dto.year.toString()) // conversione automatica

        // 4 set immagine con glide OK
        Glide.with(holder.itemView.context)
            .load(dto.getImageUrl())
            .into(imageVH)


        // OK
        TODO("click VH, apertura Details, passa id ")
        holder.itemView.setOnClickListener {v:View?->
            callbackVH!!.esegui(dto.ids.trakt)
        }

    }



    // funzione che fa copia elementi list
    fun updateList(inputList: List<PopularDtoK>) {
        adapterList.clear() // Rimuove tutti gli elementi dalla lista attuale
        adapterList.addAll(inputList) // Aggiunge tutti gli elementi della nuova lista
    }


    /* caso var adapterList TODO eugi
    fun updateList(inputList: List<PopularDtoK>) {
        Crea una copia indipendente di inputList e assegna questa nuova lista a adapterList
        adapterList = inputList.toList().toMutableList() ????
    }
     */

    // CALLBACK
    // TODO altro modo, lambda???
    // TODO laternit o CallbackVH? nullable ???

    lateinit var callbackVH: CallbackVH

    fun setCallbackVH(callbackVH: CallbackVH) {
        this.callbackVH = callbackVH
    }

}

