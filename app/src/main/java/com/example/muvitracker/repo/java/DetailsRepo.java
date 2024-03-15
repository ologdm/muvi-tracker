package com.example.muvitracker.repo.java;

import com.example.muvitracker.repo.dto.DetailsDto;
import com.example.muvitracker.utils.java.MyRetrofit;
import com.example.muvitracker.utils.java.MyRetrofitCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//              3°STEP
// 1. Argomenti nuovi:
//      Aggiungi @Path, @Query, @Headers(sopra GET)/Header(come path), @Body
//      Aggiungi Headers direttamente in Retrofit (eugi) - da fare copia incolla no studio

// 2. Sposta Retrofit (base) direttamente in altra funzione



public class DetailsRepo {

    // 1.2
    private static DetailsRepo istance;
    private DetailsRepo() {}
    public static DetailsRepo getIstance() {
        if (istance == null) {
            istance = new DetailsRepo();
        }
        return istance;
    }



    // 2. METODI


    // 2.2 Retrofit
    Retrofit retrofit = MyRetrofit.getRetrofit();

    TraktApi traktApi = retrofit.create(TraktApi.class);


    public void callDetailsApi(int traktMovieId, MyRetrofitCallback<DetailsDto> myRetrofitCallback) {

        Call<DetailsDto> detailsCall = traktApi.getDetailsDto(traktMovieId);

        detailsCall.enqueue(new Callback<DetailsDto>() {
            @Override
            public void onResponse(Call<DetailsDto> call, Response<DetailsDto> response) {
                if (response.isSuccessful()){
                    myRetrofitCallback.onSuccess(response.body());
                } else {
                    // !!! Messaggio inviato al presenter in caso di errore server
                    myRetrofitCallback.onError(new Throwable("response unsuccessful"));
                }
            }

            @Override
            public void onFailure(Call<DetailsDto> call, Throwable t) {
                t.printStackTrace();
                myRetrofitCallback.onError(t);
            }
        });
    }



}
