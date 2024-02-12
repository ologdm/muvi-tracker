package com.example.muvitracker.repository;

import com.example.muvitracker.repository.dto.MovieDto;
import com.example.muvitracker.utils.MyRetrofit;
import com.example.muvitracker.utils.MyRetrofitListCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

// singleton - istanza statica di se stessa, costruttore privato, creo istanza solo se istanza==null
// lista non serve -> passo direttamente callback dove mi serve quando ho i dati pronti

// **MovieDto**

public class PopularRepo {

    // SINGLETON
    // istanza statica, costruttore privato, metodo che costruisce l'istanza statica
    // attrib statico - utilizzata da tutte le istanze della classe
    public static PopularRepo istance;

    private PopularRepo(){};
    // motodo static -> si puo chiamare da blueprint
    public static PopularRepo getIstance (){
        // se nulla la creo
        if (istance==null){
            istance = new PopularRepo();
        }
        return istance; // dammi istanza comune del blueprint
    }



    /*
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://private-anon-5a4b7269e2-trakt.apiary-mock.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

     */


    // RETROFIT
    // 3.1
    Retrofit retrofit = MyRetrofit.getRetrofit();

    // 3.2 creo api
    TraktApi traktApi = retrofit.create(TraktApi.class);

    // 3.3/3.4 chiamata popular
    public void callPopular(MyRetrofitListCallback<MovieDto> callback) {
        // enqueue si fa sempre su una nuova call, riutilizzare la call non si puo fare
        Call<List<MovieDto>> popularCall = traktApi.getPopularMovies();

        popularCall.enqueue(new Callback<List<MovieDto>>() {
            @Override
            public void onResponse(Call<List<MovieDto>> call, Response<List<MovieDto>> response) {
                // caricamento
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                    System.out.println("DIMA REPOSITORY ");
                } else {
                    // response fornira l'ecception; TODO dove la scrivo???
                    callback.onError(new HttpException(response));
                }
            }
            @Override
            public void onFailure(Call<List<MovieDto>> call, Throwable t) {
                // dove lo mostro??
                callback.onError(t);
            }
        });
    }


}
