package com.example.muvitracker.repo.java;

import com.example.muvitracker.repo.java.dto.BoxofficeDto;
import com.example.muvitracker.utils.java.MyRetrofit;
import com.example.muvitracker.utils.java.MyRetrofitListCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

//              2°STEP
// 1. **BoxofficeDto**
// 2. Singleton
// 3. Callback passaggio dati a presenter


//              3°STEP
// 1. Spostamento Retrofit (parte fissa) in classe statica
// 2.


// !!!
// static BoxofficeRepository istance = new BoxofficeRepository();
// equivalente del singleton, si usa il metodo invece di questo
// il metodo permette anche di passare dei paramentri al getIstance(par1,par2)


public class BoxofficeRepo {


    // 1. ATTRIBUTI
    // 1.1 Singleton
    static BoxofficeRepo istance;

    private BoxofficeRepo() {
    }

    public static BoxofficeRepo getIstance() {
        if (istance == null) {
            istance = new BoxofficeRepo();
        }
        return istance;
    }



    // 2. METODI

    // 2.1 Creo Retrofit
    Retrofit retrofit = MyRetrofit.getRetrofit();

    // 2.2 Creo Api
    TraktApiInterface traktApi = retrofit.create(TraktApiInterface.class);

    // 2.3 Chiamata BoxofficeApi
    public void callBoxoffice(MyRetrofitListCallback<BoxofficeDto> callback) {

        Call<List<BoxofficeDto>> boxofficeCall = traktApi.getBoxofficeMovies();

        boxofficeCall.enqueue(new Callback<List<BoxofficeDto>>() {
            @Override
            public void onResponse(Call<List<BoxofficeDto>> call, Response<List<BoxofficeDto>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    // !!! risposta server 4xx, 5xx
                    callback.onError(new HttpException(response));
                }
            }

            @Override
            public void onFailure(Call<List<BoxofficeDto>> call, Throwable t) {
                // !!! no risposta server
                callback.onError(t);
            }
        });


    }

}




