package com.example.muvitracker.mainactivity.boxofficefragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.repository.dto.BoxofficeDto;

import java.util.ArrayList;
import java.util.List;

// OK

public class BoxofficeAdapter extends RecyclerView.Adapter {

    List<BoxofficeDto> boxofficeList = new ArrayList<>();


    // 1 OK
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_boxoffice_grid,
            parent, false);
        return new BoxofficeVH(view);
    }


    // 2
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

    }


    // 3
    @Override
    public int getItemCount() {
        return boxofficeList.size();
    }



    // OK
    public void updateAdapter(List<BoxofficeDto> list) {
        boxofficeList = list;
        notifyDataSetChanged();
    }


}

