package com.example.muvitracker.mainactivity.kotlin.sear

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.mainactivity.java.search.SearchVH
import com.example.muvitracker.repo.java.dto.search.SearchDto
import com.example.muvitracker.repo.kotlin.dto.search.SearDto

/** Dto - 2 tipi, struttura uguale:
 * - movie
 * - show -> serie con classe episodi separate
 *
 */


// binding:
// 1. ( binding = VhSearchBinding.inflate ) -> al posto di ( view = Layoutinflater.inflate ), binding al posto di view
// 2. ( root.setOnClickListener ) - radice vista layout,  equivalmente di ( holder.itemView.setOnClickListener )


class SearAdapter : RecyclerView.Adapter<SearVH>() {


    private val adapterList = mutableListOf<SearDto>()



    // create - stile ViewBinding OK
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearVH {

        val binding = VhSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // VhSearchBinding.inflate -> al posto di Layoutinflater
        return SearVH(binding)
    }


    // OK
    override fun getItemCount(): Int {
        return adapterList.size
    }


    // OK
    override fun onBindViewHolder(holder: SearVH, position: Int) {

        val dto = adapterList[position] // syntactic sugar of list.get(index)

        // new - binding
        with(holder.binding) {// prendo binding dal vh creato

            // devo gestire 2 casi - movie e show
            println("dto ${dto.movie}")
            typeObject.text = dto.type
            scoreVH.text= dto.score.toString()

            if (dto.movie != null) {

                titleVH.text = dto.movie.title
                yearVH.text = dto.movie.year.toString()

                Glide.with(holder.itemView.context).load(dto.movie.imageUrl()).into(imageVH)
                //println(dto.movie.imageUrl())

            }
            // TODO: SHOWs -> da implementare prima grafment a parte
            else if (dto.show != null) {
                typeObject.text = dto.type
                titleVH.text = dto.show.title
                yearVH.text = dto.show.year.toString()

                Glide.with(holder.itemView.context).load(dto.show.imageUrl()).into(imageVH)
                //println(dto.show.imageUrl())
            }



            // (root) - radice vista layout,  equivalmente di (holder.itemView)
            // passo id -> apro details
            root.setOnClickListener {
               if (dto.movie!= null) {
                   callbackVH?.invoke(dto.movie.ids.trakt ?: return@setOnClickListener)
               }
               else if (dto.show!= null){
                   callbackVH?.invoke(dto.show.ids.trakt ?: return@setOnClickListener)
               }


                //  return@setOnClickListener -> esci da funzione
            }

        }

    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateList (list: List<SearDto>){
        adapterList.clear()
        adapterList.addAll(list)
        // pulisco lista mutabile, aggiungo elementi da list a mutabile
        notifyDataSetChanged()
    }


    // CALLBACK - per aprire details al click
    private var callbackVH : ((Int)->Unit)? = null

    fun setCallbackVH (call:(Int)->Unit){
        callbackVH = call
    }







}