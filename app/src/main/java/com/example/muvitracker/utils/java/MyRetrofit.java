package com.example.muvitracker.utils.java;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyRetrofit {


    // public static MyRetrofit istance = new MyRetrofit();

    // singleton
    private static Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.trakt.tv/")
        .addConverterFactory(GsonConverterFactory.create())
        // Add Headers -> Trakt-Api-Key
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
        // #############################
        .build();


    public static Retrofit getRetrofit (){
        return retrofit;
    }


    }
