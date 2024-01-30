package com.example.muvitracker.mainactivity.popularfragment;


import com.example.muvitracker.repository.MyRetrofitCallback;
import com.example.muvitracker.repository.Repository1Popular;
import com.example.muvitracker.repository.dto.MovieDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 3 tipi
// http - 4xx/5xx
//IOException - input output, internet
//


public class PopularPresenter implements PopularContract.Presenter {

    // attributi
    PopularContract.View view;
    Repository1Popular repositoryPopular =
        Repository1Popular.getIstance();

    List<MovieDto> listaPresenter = new ArrayList<>();


    // costruttore
    public PopularPresenter(
        PopularContract.View v) {
        this.view = v;
    }


    // metodi
    @Override
    public void serverCallAndUpdateUi() {
        // mostra progression + nascondi erroPage
        // view.setProgressBar(VisibilityEnum.SHOW);
        // view.setErrorPage(VisibilityEnum.HIDE);

        repositoryPopular.callPopular(
            new MyRetrofitCallback<MovieDto>() {

                @Override
                public void onSuccess(List<MovieDto> list) {
                    view.updateUi(list);
                    listaPresenter = list;
                    System.out.println("prova dati su adapter");
                    // progression nascondi
                    // view.setProgressBar(VisibilityEnum.HIDE);
                }

                @Override
                public void onError(Throwable throwable) {
                    if (throwable instanceof IOException) {
                        // errorr comunicazione, no internet
                        // mostra error
                        // view.setErrorPage(VisibilityEnum.SHOW);
                    }
                }
            });
    }
}
