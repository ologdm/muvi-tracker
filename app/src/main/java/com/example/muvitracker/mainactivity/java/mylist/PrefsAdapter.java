package com.example.muvitracker.mainactivity.java.mylist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.repo.java.dto.DetailsDto;
import com.example.muvitracker.utils.java.CallbackLiked;
import com.example.muvitracker.utils.java.CallbackVH;
import com.example.muvitracker.utils.java.CallbackCheckbox;

import java.util.ArrayList;
import java.util.List;

// 1. updateList - con copia lista e non assegnazione
// 2. clickVH - callback
// 3. checkboxClick -> callback


public class PrefsAdapter extends RecyclerView.Adapter {


    List<DetailsDto> adapterList = new ArrayList<>();


    // 2. METODI ADAPTER

    // 2.1 Crea
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.vh_prefs, parent, false);

        return new PrefsVH(view);
    }


    // 2.2 Logica
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        DetailsDto dto = adapterList.get(position);

        ImageView imageVH = holder.itemView.findViewById(R.id.imageVH);
        TextView titleVH = holder.itemView.findViewById(R.id.titleAndYear);
        CheckBox checkBoxVH = holder.itemView.findViewById(R.id.checkBox);
        ImageButton likedButton = holder.itemView.findViewById(R.id.likedButton); // D


        // TODO FAVORITE
        Context context = holder.itemView.getContext();

        final Drawable iconFilled = context.getDrawable(R.drawable.baseline_favorite_24);
        final Drawable iconEmpty = context.getDrawable(R.drawable.baseline_favorite_border_24);


        // update liked all'apertura
        updateFavoriteIcon(likedButton, dto.isLiked(), iconFilled, iconEmpty);


        // 2. set immagine + testo composto
        Glide.with(holder.itemView.getContext())
            .load(dto.getImageUrl())
            .into(imageVH);


        // titolo  + anno
        String testo = dto.getTitle() + " (" + dto.getYear() + ")";
        titleVH.setText(testo);


        // 4. click VH - passo id elemento clickato
        holder.itemView.setOnClickListener(v -> {
            callbackVH.esegui(dto.getIds().getTrakt());
        });


        // 5. click watched
        checkBoxVH.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Documentazione:
            // @param buttonView - The compound button view whose state has changed.
            // @param isChecked  - The new checked state of buttonView.

            // !!! uso if() per ovviare al problema del bug (fai azione per ogni elemento lista)
            // 5.1 se (dto.stato != stato view)

            if (dto.isWatched() != isChecked) {

                // 5.2 sincronizzo stato sul dto di list
                dto.setWatched(isChecked);

                // 5.3 passo lista aggiornata con callback -> a repository
                //faccio copia

                callbackCheckbox.play(adapterList);

                // TODO debugging - fare chech se su details si aggiorna
            }
        });

        checkBoxVH.setChecked(dto.isWatched());  // aggiorno la casella, altrimenti alla riapertura fa sempre su unchecked


        // TODO like Button
        likedButton.setOnClickListener(v -> {
            // ->> passo dto-> fragment -> repo per aggiornamento
            callbackLiked.esegui(dto);
            // <<- sul fragment aggiorno lista da repo


            // aggiorno icona con i dati nuovi ricevuti
            updateFavoriteIcon(likedButton, dto.isLiked(), iconFilled, iconEmpty);


        });


    }


    // 2.3 Conta
    @Override
    public int getItemCount() {
        return adapterList.size();
    }


    // 3. MY METHODS
    // 3.1
    public void updateList(List<DetailsDto> prefList) {
        // elimina tutti elementi lista vecchia
        // aggiunge tutti gli elementi dell'altra lista a questa
        // aggiunge in fondo.

        // copia, ma cambio dati (il riferimento si conserva) // **altro caso copia istanza nuova, nuovo riferimento
        this.adapterList.clear();
        //System.out.println(prefList.toString());
        this.adapterList.addAll(prefList);


        notifyDataSetChanged();
    }


    // 4. CALLBACKS

    // 4.1 VHolder - passo id
    CallbackVH callbackVH;

    public void setCallbackVH(CallbackVH callbackVH) {
        this.callbackVH = callbackVH;
    }


    // 4.2 Checkbox
    // per passare la lista
    CallbackCheckbox<DetailsDto> callbackCheckbox;

    public void setCallbackCheckbox(CallbackCheckbox<DetailsDto> callbackCheckbox) {
        this.callbackCheckbox = callbackCheckbox;
    }


    // 4.3 Liked - passo dto
    CallbackLiked callbackLiked;

    public void setCallbackLiked(CallbackLiked callbackLiked) {
        this.callbackLiked = callbackLiked;
    }


    // TODO aggiorna icone (su onBin)
    public void updateFavoriteIcon(
        ImageButton likedButton,
        boolean isLiked,
        Drawable iconFilled,
        Drawable iconEmpty
    ) {

        if (isLiked) {
            likedButton.setImageDrawable(iconFilled); // piena
        } else {
            likedButton.setImageDrawable(iconEmpty); // vuota
        }
    }


}
