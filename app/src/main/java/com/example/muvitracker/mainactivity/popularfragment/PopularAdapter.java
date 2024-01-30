package com.example.muvitracker.mainactivity.popularfragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvitracker.R;
import com.example.muvitracker.repository.dto.MovieDto;

import java.util.ArrayList;
import java.util.List;


// == quello di main
// spostare onBind su VH

// fatto tutte le funzioni senza guardare

public class PopularAdapter extends RecyclerView.Adapter {

    List<MovieDto> listPopular = new ArrayList<>();



    // 1.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.viewholder_popular,parent,false);
        // attachToRoot:
        // true -  true, the inflated view will be attached to the parent ViewGroup.
        // false - false, the inflated view will not be attached to the parent ViewGroup
        // more common false.
        PopularViewholder popularViewholder = new PopularViewholder(view);
        return popularViewholder;
    }


    // 2.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // prendo dato singolo da lista e lo gestisco
        MovieDto dataModelPopular = listPopular.get(position);

        // identifico pezzi view nel VH
        TextView titolo = holder.itemView.findViewById(R.id.titoloVH);
        TextView anno = holder.itemView.findViewById(R.id.annoVH);

        // passo i dati su textview
        titolo.setText(dataModelPopular.getTitle());
        anno.setText(String.valueOf(dataModelPopular.getYear()));  // convertire int in Stringa

        // TODO - click su oggetto, ma forse non serve - details usa un altro api
    }


    // 3.
    @Override
    public int getItemCount() {
        return listPopular.size();
    }



    // AGGIORNA LISTA VISUALIZZATA
    public void updateList(List<MovieDto> list){
        this.listPopular = list;
        notifyDataSetChanged();
        System.out.println("prova");
    }


}
