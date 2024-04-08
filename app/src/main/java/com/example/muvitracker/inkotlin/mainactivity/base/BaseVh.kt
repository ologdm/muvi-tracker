package com.example.muvitracker.inkotlin.mainactivity.base

import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.databinding.VhBaseBinding

// koltin
// perche il padre ha un costruttore che deve essere per forza invocato (come super in java)

class BaseVh(

    //itemView: View
    val binding: VhBaseBinding

) : RecyclerView.ViewHolder(

    //itemView
    binding.root

)



