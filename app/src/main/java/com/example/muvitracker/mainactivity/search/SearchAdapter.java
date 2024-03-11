package com.example.muvitracker.mainactivity.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.repo.dto.search.SearchDto;
import com.example.muvitracker.utils.CallbackVH;

import java.util.ArrayList;
import java.util.List;

//             4°STEP
// 1. SearchDto - solo movie e show
// 2. usare 2 casi di getData() da Dto:
//       - per aggiornare gli VH
//       - callback -> passa dati a details
// 3. glide 2 casi, usato OMDb.

// TODO
//  1. ricerca persone, VH per persona -> usare funzione getItemViewType()


public class SearchAdapter extends RecyclerView.Adapter {


    // 1. ATTRIBUTI
    private List<SearchDto> list = new ArrayList<>();


    // 2. COSTRUTTORE no


    // 3. METODI
    // 3.1 Crea
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.vh_search, parent, false);
        return new SearchVH(view);
    }


    // 3.2 Ricicla
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // 1.
        SearchDto dto = list.get(position);

        // 2. Identificazione view vh
        ImageView imageVH = holder.itemView.findViewById(R.id.imageVH);
        TextView titoloVH = holder.itemView.findViewById(R.id.titoloVH);
        TextView annoVH = holder.itemView.findViewById(R.id.annoVH);
        TextView typeObjectVH = holder.itemView.findViewById(R.id.typeObject);



        //  TODO set testo 2 casi: movie + show
        typeObjectVH.setText(dto.getType());
        if (dto.getMovie() == null) {
            titoloVH.setText(dto.getShow().getTitle());
            annoVH.setText(String.valueOf(dto.getShow().getYear())); // int
            Glide.with(holder.itemView.getContext())
                .load(dto.getShow().getImageUrl())
                .into(imageVH);
        } else {
            titoloVH.setText(dto.getMovie().getTitle());
            annoVH.setText(String.valueOf(dto.getMovie().getYear())); // int

            Glide.with(holder.itemView.getContext())
                .load(dto.getMovie().getImageUrl())
                .into(imageVH);

        }

        // VH click passa Id -> 2 casi movie e show
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // !!!!
                if (dto.getMovie() == null) {
                    callbackVH.esegui(dto.getShow().getIds().getTrakt());
                } else {
                    callbackVH.esegui(dto.getMovie().getIds().getTrakt());
                }
            }
        });


    }


    // 3.3 Conta
    @Override
    public int getItemCount() {
        return list.size();
    }


    // 4. MY METHOD

    public void updateList(List<SearchDto> list) {
        this.list = list;
        notifyDataSetChanged();
        System.out.println("test");
    }


    // 5. CALLBACK
    CallbackVH callbackVH;

    public void setCallbackVH(CallbackVH call) {
        callbackVH = call;
    }


}
