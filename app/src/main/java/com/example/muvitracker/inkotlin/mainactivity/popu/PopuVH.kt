package com.example.muvitracker.inkotlin.mainactivity.popu

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.databinding.VhPopuBinding

// koltin
// perche il padre ha un costruttore che deve essere per forza invocato (come super in java)

class PopuVH(

    //itemView: View
    val binding: VhPopuBinding

) : RecyclerView.ViewHolder(

    //itemView
    binding.root

)



