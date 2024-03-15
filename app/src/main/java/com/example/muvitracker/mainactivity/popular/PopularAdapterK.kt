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


// kotlin
// 1) conversione automatica Int ==> annoVH.setText(dto.year.toString()) ==> //
// 2)


class PopularAdapterK : RecyclerView.Adapter<PopularVHK>() {


    // lista OK
    private val adapterList = mutableListOf<PopularDtoK>()

    // variante
    // private var adapterList = listOf<PopularDtoK>()


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


        holder.itemView.setOnClickListener {
            callbackVH?.invoke(dto.ids.trakt)
        }

            // CALLBACKS
            // callback () - niente
            // callback (1 par) ==> it ==> paramentro default callback () ]

            //callbackVH!!.esegui(dto.ids.trakt)
            // callbackVH?.invoke(dto.ids.trakt)
            // tipo nullable -> invoke()  invoca una callback
            // tipo not nullable -> uso invoke() oppure callbackVH(dto.ids.trakt)


    }


    // OK
    fun updateList(inputList: List<PopularDtoK>) {
        // 1 variante - val mutable
        adapterList.clear() // Rimuove tutti gli elementi dalla lista attuale
        adapterList.addAll(inputList) // Aggiunge tutti gli elementi della nuova lista

        //2 variante - var list
        //adapterList = inputList.toList() // sotituisco lista con copia lista passata
    }


    // (eugi) -> nullable o costruttore
    // definisco una var callback e il tipo callback
    // attrav una lambda con parametro intero e ritorno unit
    // data la callback nullable, uso ((Int) -> Unit)?

    //lateinit var callbackVH: CallbackVHK => usare meno possibile
    // lateinit var allbackVH: (Int) -> Unit //=> non null da errore


    private var callbackVH: ((Int) -> Unit)? = null



    // quando sta settando voglio mettere un valore non null ,
    // quindi (Int) -> Unit) senza ?
    fun setCallbackVH(callbackVH: (Int) -> Unit) {
        this.callbackVH = callbackVH
    }

    /* Interfaccia Eliminata
fun setCallbackVHK(callbackVH: CallbackVHK) {
    this.callbackVH = callbackVH
}
 */


}


// APPUNTI ############################################################

// COPIA (eugi)
// 1. val mutableList SI
// adapterList.clear()
// adapterList.addAll(inputList)

// 2. var List SI
// adapterList = inputList.toList()

// 3 var mutableList NO
// si evita di fare la doppia mutazione


/* CARATTERI SPECIALI (eugi)
* ! - il compilatore non sa se il tipo e nullable o no => Tipo!
* ? - tipo nullable => Tipo?
* ?. - chiama se istanza non nulla   => instanza?.funzione()
* !!. - garantisce istanza esistente => istanza!!.funzione()

* es1
    callbackVH?.invoke(dto.ids.trakt)

    if (callbackVH != nul) {
        callbackVH.invoke(dto.ids.traky)
     }

* es2
    callbackVH!!.invoke(dto.ids.trakt)

    if (callbackVH == null) {
        throw Exception("messaggio")
    }
    callback.invoke(dto.ids.trakt)

 */


/* CALLBACK COME LAMBDA (eugi)
interface CallbackVH {
    fun esegui (int traktMovieId);
}

// laternit - usare meno possibile

// 1)implementa interfaccia
lateinit var callbackVH: CallbackVHK

// 2) implementa lambda
lateinit var callbackVH: (Int) -> Unit

// 3) nullable o costruttore - meglio
private var callbackVH: ((Int) -> Unit)? = null

// 4) non nullable -
lateinit var callbackVH: (Int) -> Unit // non null da errore

 */





