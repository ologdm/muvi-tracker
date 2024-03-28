package com.example.muvitracker.injava.mainactivity.search;

import com.example.muvitracker.injava.repo.SearchRepo;
import com.example.muvitracker.injava.utils.MyRetrofitListCallback;

import java.util.List;


// 1. -> vedere in Fragment
// 2. metodo passa stringa a repository.setSearchText()
// TODO  - 1. debounce

public class SearchPresenter implements SearchContract.Presenter {


    // 1. ATTRIBUTI
    private SearchContract.View view;
    private SearchRepo repository = SearchRepo.getIstance();


    // 2. COSTRUTTORE
    public SearchPresenter(SearchContract.View view) {
        this.view = view;
    }


    // 3. METODI
    // 3.1
    @Override
    public void callServerAndUpdateUi() {


        repository.chiamataSearch(new MyRetrofitListCallback() {
            @Override
            public void onSuccess(List list) {
                view.updateUi(list);

            }


            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();

            }
        });
    }


    // 3.2
    @Override
    public void onVHolderClick(int id) {
        view.startDetailsFragment(id);
    }



    // 3.3 Invia stringa ricerca
    @Override
    public void setSearchText(String searchText){
        repository.setSearchText(searchText);
    }

    // TODO 1 - debounce




}
