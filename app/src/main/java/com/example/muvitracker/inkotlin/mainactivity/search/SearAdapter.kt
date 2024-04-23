package com.example.muvitracker.inkotlin.mainactivity.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhSearchBinding
import com.example.muvitracker.inkotlin.model.dto.search.SearDto

/** Dto - 1 tipi attualmente:
 * - movie
 * TODO shows, episods
 */


// binding:
// 1. ( binding = VhSearchBinding.inflate ) -> al posto di ( view = Layoutinflater.inflate ), binding al posto di view
// 2. ( root.setOnClickListener ) - radice vista layout,  equivalmente di ( holder.itemView.setOnClickListener )

class SearAdapter : RecyclerView.Adapter<SearVH>() {

    private val adapterList = mutableListOf<SearDto>()
    private var callbackVH: ((Int) -> Unit)? = null


    // 1. create - con binding OK
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearVH {
        val binding = VhSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearVH(binding)
    }


    // 2. OK
    override fun getItemCount(): Int {
        return adapterList.size
    }

    // 3. OK
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearVH, position: Int) {

        val dto = adapterList[position] // syntactic sugar of list.get(index)


        with(holder.binding) {// prendo binding dal vh creato
            typeObjectVH.text = dto.type
            scoreVH.text = dto.score.toString()

            if (dto.movie != null) {
                titleVH.text = "${dto.movie.title} ${dto.movie.year.toString()}"
                Glide.with(holder.itemView.context)
                    .load(dto.movie.imageUrl())
                    .placeholder(R.drawable.test)
                    .into(imageVH)
            }

            // clickVH
            root.setOnClickListener {
                callbackVH?.invoke(dto.movie?.ids?.trakt ?: return@setOnClickListener)
                // evis operator - esci da funzione
            }
        }
    }


    // ALTRI METODI
    fun updateList(list: List<SearDto>) {
        adapterList.clear()
        adapterList.addAll(list)
        notifyDataSetChanged()
    }

    fun setCallbackVH(call: (Int) -> Unit) {
        callbackVH = call
    }


}