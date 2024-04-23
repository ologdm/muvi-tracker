package com.example.muvitracker.inkotlin.mainactivity.base

import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.databinding.VhBaseBinding

class BaseVh(val binding: VhBaseBinding) : RecyclerView.ViewHolder(binding.root)

// costruttore figlio - passo binding
// costruttore padre(richiede itemview) -> binding.root==itemview

/* IN JAVA
public class BaseVh extends RecyclerView.ViewHolder {

    public BaseVh(VhBaseBinding binding) {
        super(binding.getRoot());
    }
 */




