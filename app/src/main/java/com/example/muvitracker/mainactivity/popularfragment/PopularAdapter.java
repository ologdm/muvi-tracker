package com.example.muvitracker.mainactivity.popularfragment;

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

import java.util.ArrayList;
import java.util.List;


// == quello di main
// spostare onBind su VH

// fatto tutte le funzioni senza guardare

public class PopularAdapter extends RecyclerView.Adapter {

    List<MovieDto> popularList = new ArrayList<>();



    // 1.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.vh_popular_grid,parent,false);
        PopularVH popularViewholder = new PopularVH(view);
        return popularViewholder;
    }


    // per creare diversi tipi di VH e gestirli,
    // in onCreateViewHolder(sopra) ho il controllo con int viewType
    // non utilizzato
    /*
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
     */



    // 2.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // prendo dato singolo da lista e lo gestisco
        MovieDto popularDto = popularList.get(position);

        // identifico pezzi view nel VH
        TextView titoloVH = holder.itemView.findViewById(R.id.titoloVH);
        TextView annoVH = holder.itemView.findViewById(R.id.annoVH);

        ImageView posterVH = holder.itemView.findViewById(R.id.imageVH);

        // Set Textview
        titoloVH.setText(popularDto.getTitle());
        annoVH.setText(String.valueOf(popularDto.getYear()));  // convertire int in Stringa

        // set Image
        Glide.with(holder.itemView.getContext())
            .load(popularDto.getImageUrl())
            .into(posterVH);


        // TODO 3°STEP
        // Click VH
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passo come parametro l' Id
                callbackVH.esegui(popularDto.getIds().getTrakt());
            }
        });


    }


    // 3.
    @Override
    public int getItemCount() {
        return popularList.size();
    }



    // Aggiorna Lista da Mostrare
    public void updateList(List<MovieDto> list){
        this.popularList = list;
        notifyDataSetChanged();
    }



    // TODO 3° STEP
    // CALLBACK -
    // devo chiamare callback implementata su PopularFragment
    // al click del VH
    // 2.Dichiarazione

    // 1. Dichiarazione
    CallbackVH callbackVH;

    // 2. Setter
    public void setCallbackVH(CallbackVH callbackVH) {
        this.callbackVH = callbackVH;
    }


    // 3. Interfaccia
    public interface CallbackVH {
        public void esegui (int movieId);
    }


}
