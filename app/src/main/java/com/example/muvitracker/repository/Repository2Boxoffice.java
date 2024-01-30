package com.example.muvitracker.repository;


// lista non serve -> passo direttamente callback dove mi serve quando ho i dati pronti
// fare come popular

import com.example.muvitracker.repository.dto.BoxofficeDto;
import com.example.muvitracker.repository.dto.MovieDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository2Boxoffice {

    // SINGLETON
    public static Repository2Boxoffice istance;
    private Repository2Boxoffice (){};
    public static Repository2Boxoffice getIstance () {
        if (istance==null){
            istance = new Repository2Boxoffice();
        }
        return istance;
    }





    // RETROFIT  -OK
    // TODO - spostare retrofit a parte
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://private-anon-5a4b7269e2-trakt.apiary-mock.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    TraktApi traktApi = retrofit.create(TraktApi.class);

    Call<List<MovieDto>> popularCall = traktApi.getPopularMovies();
    Call<List<BoxofficeDto>> boxofficeCall = traktApi.getBoxofficeMovies();


    public void callBoxoffice(MyRetrofitCallback<BoxofficeDto> callback) {  // da implementare

        boxofficeCall.enqueue(new Callback<List<BoxofficeDto>>() {
            @Override
            public void onResponse(Call<List<BoxofficeDto>> call, Response<List<BoxofficeDto>> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new HttpException(response));
                }
            }

            @Override
            public void onFailure(Call<List<BoxofficeDto>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }



}
