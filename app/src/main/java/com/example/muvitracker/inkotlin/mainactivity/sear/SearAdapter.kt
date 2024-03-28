package com.example.muvitracker.inkotlin.mainactivity.sear

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.inkotlin.repo.dto.search.SearDto

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
            scoreVH.text = dto.score.toString()

            if (dto.movie != null) {
                titleVH.text = dto.movie.title
                yearVH.text = dto.movie.year.toString()
                Glide.with(holder.itemView.context).load(dto.movie.imageUrl()).into(imageVH)
                //println(dto.movie.imageUrl())
            }
            // TODO: SHOWs
            else if (dto.show != null) {
                typeObject.text = dto.type
                titleVH.text = dto.show.title
                yearVH.text = dto.show.year.toString()
                Glide.with(holder.itemView.context).load(dto.show.imageUrl()).into(imageVH)
                //println(dto.show.imageUrl())
            }


            // caso movie
            root.setOnClickListener {
                callbackVH?.invoke(dto.movie?.ids?.trakt ?: return@setOnClickListener)
                // evis operator - esci da funzione
            }

            // TODO: with type - SHOWS OK
            root.setOnClickListener {
                if (dto.movie != null) {
                    callbackVHtype?.invoke(
                        dto.movie.ids.trakt ?: return@setOnClickListener,
                        dto.type
                    )
                } else if (dto.show != null) {
                    callbackVHtype?.invoke(
                        dto.show.ids.trakt ?: return@setOnClickListener,
                        dto.type
                    )
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<SearDto>) {
        adapterList.clear()
        adapterList.addAll(list)
        // pulisco lista mutabile, aggiungo elementi da list a mutabile
        notifyDataSetChanged()
    }


    // CALLBACK - per aprire details al click
    private var callbackVH: ((Int) -> Unit)? = null

    fun setCallbackVH(call: (Int) -> Unit) {
        callbackVH = call
    }


    // TODO: with type - SHOWS OK
    private var callbackVHtype: ((Int, String) -> Unit)? = null

    fun setCallbackVHtype(call: (Int, String) -> Unit) {
        callbackVHtype = call
    }


}