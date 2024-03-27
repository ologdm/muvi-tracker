package com.example.muvitracker.mainactivity.kotlin.boxo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.repo.kotlin.dto.BoxoDto


class BoxoAdapter : RecyclerView.Adapter <BoxoVH>() {

    // variante 1 popular
    // variante 2 - var/list
    private var adapterList = listOf<BoxoDto>()


    // dichiaro lambda callback
    var callbackVH : ((movieId:Int)->Unit)? = null


    // METODI ADAPTER

    // crea vh OK
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoxoVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.vh_boxo,parent,false)
        return BoxoVH(view)
    }

    // conta elementi da gestire OK
    override fun getItemCount(): Int {
       return adapterList.size
    }

    // logica VH
    override fun onBindViewHolder(holder: BoxoVH, position: Int) {

        val dto : BoxoDto = adapterList.get(position)

        // inizializzaz interni vh OK
        val titoloVH: TextView = holder.itemview.findViewById(R.id.titleVH)
        val annoVH : TextView = holder.itemview.findViewById(R.id.yearVH)
        val imageVH :ImageView = holder.itemview.findViewById(R.id.imageVH)

        // set elementi da dto OK
        titoloVH.text = dto.movie.title
        annoVH.text = dto.movie.year.toString() // toString obbligatorio per Int

        // GLide - utilizzare funzione context, load, into  OK
        Glide.with(holder.itemView.context)
            .load(dto.movie.getImageUrl())
            .into(imageVH)


        // click VH
        holder.itemview.setOnClickListener {  // view
            // OnClickListener -> ()->Unit
            // itemview funzione {chiama callback al click su oggetto}
            // par it - non utilizzato

            // chiama callbackVH -
            // passo elemento nella chiamata
            callbackVH?.invoke(dto.movie.ids.trakt)
        }


    }



    // update con copia OK
    @SuppressLint("NotifyDataSetChanged")
    fun updateList (list: List<BoxoDto>){
        adapterList= list.toList() // sotituisco lista con la  copia nuova istanza
        notifyDataSetChanged()

    }


    // lambda set
    fun setcallbackVH (call:(movieId:Int)->Unit){
        callbackVH=call

    }







}



