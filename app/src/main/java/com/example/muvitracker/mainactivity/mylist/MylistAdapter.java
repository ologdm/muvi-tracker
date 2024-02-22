package com.example.muvitracker.mainactivity.mylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.utils.CallbackVH;
import com.example.muvitracker.utils.CallbackCheckbox;

import java.util.ArrayList;
import java.util.List;

// 1. updateList - con copia lista e non assegnazione
// 2. clickVH - callback
// 3. checkboxClick -> callback


public class MylistAdapter extends RecyclerView.Adapter {

    // 1. ATTRIBUTI
    List<MylistDto> prefList = new ArrayList<>();


    // 2. METODI ADAPTER

    // 2.1 Crea
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.vh_mylist, parent, false);
        return new MylistVH(view);
    }


    // 2.2 Logica
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MylistDto dto = prefList.get(position);

        // 1. identificazione
        ImageView imageVH = holder.itemView.findViewById(R.id.imageVH);
        TextView titleVH = holder.itemView.findViewById(R.id.titleAndYear);
        CheckBox checkBoxVH = holder.itemView.findViewById(R.id.checkBox);


        // 2. set immagine + testo composto
        Glide.with(holder.itemView.getContext())
            .load(dto.getDetailsDto().getImageUrl())
            .into(imageVH);

        // titolo  + anno
        String testo = dto.getDetailsDto().getTitle() + " (" + dto.getDetailsDto().getYear() + ")";
        titleVH.setText(testo);


        // 4. click VH - passo id elemento clickato
        holder.itemView.setOnClickListener(v -> {
            callbackVH.esegui(dto.getDetailsDto().getIds().getTrakt());
        });


        // 5. click watched
        checkBoxVH.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Documentazione:
            // @param buttonView - The compound button view whose state has changed.
            // @param isChecked  - The new checked state of buttonView.

            // !!! uso if() per ovviare al problema del bug (fai azione per ogni elemento lista)
            // 5.1 se (dto.stato != stato view)
            if (dto.isWatched() != isChecked) {
                // 5.2 sincronizzo stato sul dto
                dto.setWatched(isChecked);
                // 5.3 passo lista aggiornata con callback -> a repository
                callbackCheckbox.play(prefList);

                // TODO debugging - fare chech se su details si aggiorna
            }
        });

        // 5.4 aggiorno la casella
        // altrimenti alla riapertura fa sempre su unchecked
        checkBoxVH.setChecked(dto.isWatched());
    }


    // 2.3 Conta
    @Override
    public int getItemCount() {
        return prefList.size();
    }


    // 3. MY METHODS
    // 3.1
    public void updateList(List<MylistDto> myList) {
        // elimina tutti elementi lsita vecchia
        // aggiunge tutti gli elementi dell'altra lista a questa
        // aggiunge in fondo.
        this.prefList.clear();
        this.prefList.addAll(myList);

        notifyDataSetChanged();
    }




    // 4. CALLBACKS

    // 4.1 VHolder
    CallbackVH callbackVH;
    public void setCallbackVH(CallbackVH callbackVH) {
        this.callbackVH = callbackVH;
    }


    // 4.2 Checkbox
    // per passare la lista
    CallbackCheckbox <MylistDto> callbackCheckbox;
    public void setCallbackCheckbox(CallbackCheckbox <MylistDto> callbackCheckbox) {
        this.callbackCheckbox = callbackCheckbox;
    }


}
