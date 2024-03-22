package com.example.muvitracker.repo.java;

import com.example.muvitracker.repo.java.dto.search.SearchDto;
import com.example.muvitracker.utils.java.MyRetrofit;
import com.example.muvitracker.utils.java.MyRetrofitListCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//          4°STEP Search
// 1. Chiamata retrofit
// 2. path aggiuntivo - solo movie, shows
// 3. Passare query ricerca a API
// 4. Singleton

public class SearchRepo {

    // 1. ATTRIBUTI
    //List<SearchDto> searchListProva = new ArrayList<>();

    private String searchText;


    // 2. COSTRUTTORE
    // 2.1 Singleton
    private static SearchRepo istance;

    private SearchRepo() {
    }

    public static SearchRepo getIstance() {
        if (istance == null) {
            istance = new SearchRepo();
        }
        return istance;
    }


    // 3. METODI
    // 3.1 Setter searchText
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }


    // 3.2 Retrofit
    // 1. Crea retrofit
    Retrofit retrofit = MyRetrofit.getRetrofit();

    // 2.Crea Api
    TraktApiInterface traktApi = retrofit.create(TraktApiInterface.class);

    // 3. Call necessaria
    public void chiamataSearch(MyRetrofitListCallback callback) {

        Call<List<SearchDto>> searchCall = traktApi.getSearch(searchText);

        searchCall.enqueue(new Callback<List<SearchDto>>() {
            @Override
            public void onResponse(Call<List<SearchDto>> call, Response<List<SearchDto>> response) {

                if (response.isSuccessful()) {

                    // OK !!! la callback posso anche non tipizzarla
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<SearchDto>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


}
