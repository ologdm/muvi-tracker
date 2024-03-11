package com.example.muvitracker.repo;

import com.example.muvitracker.repo.dto.PopularDto;
import com.example.muvitracker.utils.MyRetrofit;
import com.example.muvitracker.utils.MyRetrofitListCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

//              2°STEP
// 1. **MovieDto**
// 2. Singleton
// 3. Callback passaggio dati a presenter



//              3°STEP
// 1. Spostamento Retrofit (parte fissa) in classe statica



// !!! Singleton
// istanza statica di se stessa, costruttore privato, creo istanza solo se istanza==null
// lista non serve -> passo direttamente callback dove mi serve quando ho i dati pronti



public class PopularRepo {

    // 1. ATTRIBUTI
    // Singleton
    // istanza statica, costruttore privato, metodo che costruisce l'istanza statica
    // attrib statico - utilizzata da tutte le istanze della classe
    public static PopularRepo istance;

    private PopularRepo(){};
    // motodo static -> si puo chiamare da blueprint
    public static PopularRepo getIstance (){
        if (istance==null){ // se nulla la creo
            istance = new PopularRepo();
        }
        return istance; // dammi istanza comune del blueprint
    }



    // 2. METODI

    // 2.1 Creo Retrofit
    // !!! singleton per tutto il programma
    Retrofit retrofit = MyRetrofit.getRetrofit();

    // 2.2 Creo Api
    TraktApi traktApi = retrofit.create(TraktApi.class);


    // 2.3 Chiamata PopularApi
    //     piu passaggio info a Presenter tramite Callback pers
    public void callPopular(MyRetrofitListCallback<PopularDto> callback) {

        // !!! enqueue si fa sempre su una nuova call, riutilizzare la call non si puo fare
        Call<List<PopularDto>> popularCall = traktApi.getPopularMovies();

        popularCall.enqueue(new Callback<List<PopularDto>>() {
            @Override
            public void onResponse(Call<List<PopularDto>> call, Response<List<PopularDto>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                    System.out.println("DIMA REPOSITORY ");
                } else {
                    // !!! risposta server 4xx, 5xx
                    // Passo l'HttpException a ->presenter ->printStackTrace
                    callback.onError(new HttpException(response));
                }
            }
            @Override
            public void onFailure(Call<List<PopularDto>> call, Throwable t) {
                // !!! no risposta server
                // Invio a presenter-> printStackTrace
                callback.onError(t);
            }
        });
    }


}
