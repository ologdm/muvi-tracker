package com.example.muvitracker.mainactivity.boxoffice.java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.repo.dto.BoxofficeDto;
import com.example.muvitracker.utils.java.CallbackVH;

import java.util.ArrayList;
import java.util.List;

//          2° STEP-> inizio
// == Popular


//          3° STEP
// == Popular


// TODO
//      1- spostare onBind su VH



public class BoxofficeAdapter extends RecyclerView.Adapter {

    // 1. ATTRIBUTI
    private List<BoxofficeDto> boxofficeList = new ArrayList<>();

    // 2. COSTRUTTORE vuoto


    // 3. METODI ADAPTER
    // 3.1 Costruttore
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_boxoffice_grid,
            parent, false);
        return new BoxofficeVH(view);
    }


    // 3.2 onBind
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BoxofficeDto dto = boxofficeList.get(position);

        TextView titoloVH = holder.itemView.findViewById(R.id.titoloVH);
        TextView annoVH = holder.itemView.findViewById(R.id.annoVH);
        ImageView posterVH = holder.itemView.findViewById(R.id.imageVH);


        titoloVH.setText(dto.getMovie().getTitle());
        annoVH.setText(String.valueOf(dto.getMovie().getYear())); // converte 'int in String'

        Glide.with(holder.itemView.getContext())
            .load(dto.getUrlImage())
            .into(posterVH);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackVH.esegui(dto.getMovie().getIds().getTrakt());
            }
        });




    }


    // 3.3 Size
    @Override
    public int getItemCount() {
        return boxofficeList.size();
    }




    // 4. MY METHODS
    // 4.1 Aggiorna Lista da mostrare
    public void updateAdapter(List<BoxofficeDto> list) {
        boxofficeList = list;
        notifyDataSetChanged();
    }



    // 5 CALLBACK
    // dichiaraz
    CallbackVH callbackVH;
    // setter
    public void setCallbackVH (CallbackVH callbackVH){
        this.callbackVH = callbackVH;
    }






}

