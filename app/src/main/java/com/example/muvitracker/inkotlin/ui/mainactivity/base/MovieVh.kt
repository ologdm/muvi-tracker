package com.example.muvitracker.inkotlin.ui.mainactivity.base

import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.databinding.VhMovieBinding

class MovieVh(val binding: VhMovieBinding) : RecyclerView.ViewHolder(binding.root)

// costruttore figlio - passo binding
// costruttore padre(richiede itemview) -> binding.root==itemview

/* IN JAVA
public class BaseVh extends RecyclerView.ViewHolder {

    public MovieVh(VhBaseBinding binding) {
        super(binding.getRoot());
    }
 */




