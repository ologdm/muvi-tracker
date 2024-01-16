package com.example.muvitracker.mainactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvitracker.R;
import com.example.muvitracker.repository.DataModel;
import com.example.muvitracker.repository.TraktApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainAdapter extends RecyclerView.Adapter {

    List<DataModel> listaFilm = new ArrayList<>();


    // UNO - creo la VH con View - di singolo elemento RV
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View myView = layoutInflater.inflate(R.layout.viewholder_muvi, parent, false);
        return new MuviViewholder(myView);
    }


    // DUE -
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 1
        DataModel dataModel = listaFilm.get(position);
        // 2
        TextView titolo = holder.itemView.findViewById(R.id.titoloVH);
        TextView anno = holder.itemView.findViewById(R.id.annoVH);
        // 3
        titolo.setText(dataModel.getTitle());
        anno.setText(String.valueOf(dataModel.getYear())); // bug risolto, int->String



        // 4 TODO CLICK ELEMENTO
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    // TRE -
    @Override
    public int getItemCount() {
        return listaFilm.size();
    }



    // UPDATE DATA
    void updateData (){
        notifyDataSetChanged();
    }



    // ##### RETROFIT #######
    // 1. istanzio retrofit
    Retrofit retrofit = new Retrofit.Builder().
        baseUrl("https://private-anon-5a4b7269e2-trakt.apiary-mock.com/").
        addConverterFactory(GsonConverterFactory.create()).
        build();

    // 2. Retrofit crea API (sotto contratto TraktApi) - factory pattern, non ci interessa come
    TraktApi traktApi = retrofit.create(TraktApi.class);

    // 3. Predisposizione istanza di Call - GET "movies/popular"
    Call<List<DataModel>> popularMoviesCall = traktApi.getPopularMovies();

    // 4.
    public void chiamataRetrofit() {
        // 4.  faccio Call
        popularMoviesCall.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                if (response.isSuccessful()) { // =200
                    listaFilm = response.body();
                    updateData();
                }
            }


            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {

            }
        });
    }




}
