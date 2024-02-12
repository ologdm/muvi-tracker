package com.example.muvitracker.repository;

import com.example.muvitracker.repository.dto.DetailsDto;
import com.example.muvitracker.utils.MyRetrofit;
import com.example.muvitracker.utils.MyRetrofitCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// Argomenti nuovi
// Aggiungi @Path, @Query, @Headers(sopra GET)/Header(come path), @Body
// Aggiungi Headers direttamente in Retrofit
// Sposta retrofit base direttamente in altra funzione



public class DetailsRepository {

    // ATTRIBUTO

    // TODO collegare con Call
    int movieId; // id trakt film selezionato


    // SINGLETON
    private static DetailsRepository istance;
    private DetailsRepository() {}
    public static DetailsRepository getIstance() {
        if (istance == null) {
            istance = new DetailsRepository();
        }
        return istance;
    }

    // GET/SET ID
    public int getMovieId() {
        return movieId;
    }
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }




    // RETROFIT
    Retrofit retrofit = MyRetrofit.getRetrofit();

    TraktApi traktApi = retrofit.create(TraktApi.class);


    public void callDetails (MyRetrofitCallback<DetailsDto> myRetrofitCallback) {
        // call - una nuova per ogni chiamata
        Call<DetailsDto> detailsCall = traktApi.getDetailsDto(movieId);

        detailsCall.enqueue(new Callback<DetailsDto>() {
            @Override
            public void onResponse(Call<DetailsDto> call, Response<DetailsDto> response) {
                if (response.isSuccessful()){
                    myRetrofitCallback.onSuccess(response.body());
                } else {
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



/*
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.trakt.tv/")
        .addConverterFactory(GsonConverterFactory.create())
        // Add Headers -> trakt-api-key
        .callFactory(new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request()
                        .newBuilder()
                        .addHeader("trakt-api-key",
                            "d3dd937d16c8de9800f9ce30270ddc1d9939a2dafc0cd59f0a17b72a2a4208fd")
                        .build();
                    return chain.proceed(newRequest);
                }
            })
            .build())
        .build();
     */