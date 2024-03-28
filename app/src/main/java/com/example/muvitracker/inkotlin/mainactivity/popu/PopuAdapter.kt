package com.example.muvitracker.inkotlin.mainactivity.popu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.databinding.VhPopuBinding
import com.example.muvitracker.inkotlin.repo.dto.PopuDto


// kotlin
// 1) conversione automatica Int ==> annoVH.setText(dto.year.toString()) ==> //
// 2)


class PopuAdapter : RecyclerView.Adapter<PopuVH>() {


    // variante 1 val/mutablelist
    private val adapterList = mutableListOf<PopuDto>()

    // variante 2 boxo
    // private var adapterList = listOf<PopularDtoK>()


    // 1 dichiarazione lambda
    //lateinit var callbackVH: ((Int) -> Unit) // non usare
    private var callbackVH: ((Int) -> Unit)? = null // va bene


    // METODI
    // 1. OK
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopuVH {
        // definisco il mio binding
        val binding = VhPopuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopuVH(binding)
    }


    // 2. OK
    override fun getItemCount(): Int {
        return adapterList.size
    }


    // 3. OK
    override fun onBindViewHolder(holder: PopuVH, position: Int) {

        // 1 prendi elemento da mostrare OK
        val dto: PopuDto = adapterList[position]


        /* 2 identifica views OK
        val titoloVH: TextView = holder.itemView.findViewById(R.id.titleVH)
        val annoVH: TextView = holder.itemView.findViewById(R.id.yearVH)
        val imageVH: ImageView = holder.itemView.findViewById(R.id.imageVH)

         */

        with(holder.binding) {

            titleVH.text = dto.title
            yearVH.text = dto.year.toString() // conversione automatica

            Glide.with(root.context)
                .load(dto.getImageUrl())
                .into(imageVH)


            // callback view - implementazione, chiamata al click
            root.setOnClickListener {
                // callbackVH - chiamata, implementazione su fragment
                callbackVH?.invoke(dto.ids.trakt)

                // => callbackVH.invoke(dto.ids.trakt) // con lateinit non serve ?
            }
        }

    }


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


    // LAMBDA IMPLEMENTAZIONE OK


    // 2 set lambda
    fun setCallbackVH(call: (Int) -> Unit) {
        this.callbackVH = call
    }

    // APPUNTI CALLBACK SOTTO ==> fatti bene

}









