package com.example.muvitracker.mainactivity.popular;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.repository.dto.MovieDto;
import com.example.muvitracker.utils.CallbackVH;

import java.util.ArrayList;
import java.util.List;

//          2° STEP-> inizio
// 1. mostrare dati API su RV

// 2. mostrare immagini su RV con GLide

// 3. fatto tutte le funzioni senza guardare

// 4. (eugi argomento) - @getItemViewType()
//                       per creare diversi tipi di VH e gestirli,
//                       in onCreateViewHolder(sopra) ho il controllo con int viewType
//                       non utilizzato
/* @Override
   public int getItemViewType(int position) {
        return super.getItemViewType(position);
   }
*/


//          3° STEP
// 1. Creare Callback per passare dati VH -> Fragment (in UiUtils)


// TODO
//  1- spostare onBind su VH



public class PopularAdapter extends RecyclerView.Adapter {


    // 1. ATTRIBUTI

    List<MovieDto> popularList = new ArrayList<>();


    // 2. COSTRUTTORE


    // 3. METODI ADAPTER
    // 3.1 Creazione
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.vh_popular_grid, parent, false);
        PopularVH popularViewholder = new PopularVH(view);
        return popularViewholder;
    }


    // 3.2 onBind
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 1 prendo dato singolo da lista e lo gestisco
        MovieDto popularDto = popularList.get(position);

        // 2 identifico pezzi view nel VH
        TextView titoloVH = holder.itemView.findViewById(R.id.titoloVH);
        TextView annoVH = holder.itemView.findViewById(R.id.annoVH);

        ImageView posterVH = holder.itemView.findViewById(R.id.imageVH);

        // 3 Set Textview
        titoloVH.setText(popularDto.getTitle());
        annoVH.setText(String.valueOf(popularDto.getYear()));  // convertire int in Stringa

        // 4 set Image
        Glide.with(holder.itemView.getContext())
            .load(popularDto.getImageUrl())
            .into(posterVH);


        //               3°STEP
        // 5 Click VH
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // !!! passo Id come parametro -> alla callback implementata su Fragment
                callbackVH.esegui(popularDto.getIds().getTrakt());
            }
        });
    }


    // 3. Size
    @Override
    public int getItemCount() {
        return popularList.size();
    }



    // 4. MY METHODS
    // 4.1 Aggiorna Lista da mostrare
    public void updateList(List<MovieDto> list) {
        this.popularList = list;
        notifyDataSetChanged();
    }



    //                3° STEP
    // 5 CALLBACK
    //   su UiUtils
    //   devo chiamare callback implementata su PopularFragment
    //   al click del VH

    // 5.1 Dichiarazione
    CallbackVH callbackVH;

    // 5.2 Setter
    public void setCallbackVH(CallbackVH callbackVH) {
        this.callbackVH = callbackVH;
    }


}
