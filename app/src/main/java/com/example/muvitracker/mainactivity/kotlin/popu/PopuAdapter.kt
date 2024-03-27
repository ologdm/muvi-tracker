package com.example.muvitracker.mainactivity.kotlin.popu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.repo.kotlin.dto.PopuDto


// kotlin
// 1) conversione automatica Int ==> annoVH.setText(dto.year.toString()) ==> //
// 2)


class PopuAdapter : RecyclerView.Adapter<PopuVH>() {


    // lista OK
    private val adapterList = mutableListOf<PopuDto>()

    // variante
    // private var adapterList = listOf<PopularDtoK>()


    // 1. OK
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopuVH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.vh_popu, parent, false)
        return PopuVH(view)
    }


    // 2. OK
    override fun getItemCount(): Int {
        return adapterList.size
    }


    // 3. OK
    override fun onBindViewHolder(holder: PopuVH, position: Int) {

        // 1 prendi elemento da mostrare OK
        val dto: PopuDto = adapterList.get(position)

        // 2 identifica views OK
        val titoloVH: TextView = holder.itemView.findViewById(R.id.titleVH)
        val annoVH: TextView = holder.itemView.findViewById(R.id.yearVH)
        val imageVH: ImageView = holder.itemView.findViewById(R.id.imageVH)

        // 3 set testo OK
        titoloVH.text = dto.title
        annoVH.text = dto.year.toString() // conversione automatica

        // 4 set immagine con glide OK
        Glide.with(holder.itemView.context)
            .load(dto.getImageUrl())
            .into(imageVH)


        // callback view - implementazione, chiamata al click
        holder.itemView.setOnClickListener {

            // callbackVH - chiamata, implementazione su fragment
            callbackVH?.invoke(dto.ids.trakt)

            // => callbackVH.invoke(dto.ids.trakt) // con lateinit non serve ?
        }


    }


    // OK
    fun updateList(inputList: List<PopuDto>) {
        // 1 variante - val mutable
        adapterList.clear() // Rimuove tutti gli elementi dalla lista attuale
        adapterList.addAll(inputList) // Aggiunge tutti gli elementi della nuova lista

        notifyDataSetChanged() // molto importante

        //2 variante - var list
        //adapterList = inputList.toList() // sotituisco lista con copia lista passata
    }


    // LAMBDA IMPLEMENTAZIONE OK
    // 1 dichiarazione lambda
    private var callbackVH: ((Int) -> Unit)? = null // va bene

    //lateinit var callbackVH: ((Int) -> Unit) // non usare


    // 2 set lambda
    fun setCallbackVH(call: (Int) -> Unit) {
        this.callbackVH = call
    }

    // APPUNTI CALLBACK SOTTO ==> fatti bene

}









