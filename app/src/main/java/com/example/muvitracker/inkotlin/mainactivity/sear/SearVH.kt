package com.example.muvitracker.inkotlin.mainactivity.sear

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.databinding.VhSearchBinding

// nuovo modo - binding
// 1.mi collego al binding del padre tramite costruttore padre
// 2. passo binding al figlio


/*   analogo java
public class SearchVH extends RecyclerView.ViewHolder {

    // Usa il binding generato per il tuo layout
    VhSearchBinding binding;

    public SearchVH(VhSearchBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
 */


class SearVH(
    val binding: VhSearchBinding // 2. passo binding al figlio
) : RecyclerView.ViewHolder(
    binding.root // 1.binding del padre
)