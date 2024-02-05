package com.example.muvitracker.mainactivity.popularfragment;


import com.example.muvitracker.utils.Visibility;
import com.example.muvitracker.utils.MyRetrofitCallback;
import com.example.muvitracker.repository.Repository1Popular;
import com.example.muvitracker.repository.dto.MovieDto;

import java.io.IOException;
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


    // costruttore
    public PopularPresenter(
        PopularContract.View v) {
        this.view = v;
    }


    //METODI
    // ** forceRefresh ** -
    // true, comportamento swipeRefresh
    // false - default
    @Override
    public void serverCallAndUpdateUi(boolean forceRefresh) {
        // mostra progression + nascondi ErrorPage(gia nascosta)

        // else obbligatorio, altrimenti le fa entrambe
        if (forceRefresh) {
            view.setProgressBar(Visibility.HIDE);
        } else {
            view.setProgressBar(Visibility.SHOW);
        }


        repositoryPopular.callPopular(new MyRetrofitCallback<MovieDto>() {

                @Override
                public void onSuccess(List<MovieDto> list) {
                    view.updateUi(list);
                    // progression nascondi
                    view.setProgressBar(Visibility.HIDE);
                    view.setErrorPage(Visibility.HIDE);
                }

                @Override
                public void onError(Throwable throwable) {
                    // devo sempre mostrare gli errori
                    throwable.printStackTrace();
                    // nascondo la RV gia caricata in caso di errore
                    view.setRvVisibility(Visibility.HIDE);

                    // errori di internet
                    if (throwable instanceof IOException) {
                        // errorr comunicazione, no internet
                        // mostra error e nascondi progression
                        view.setErrorPage(Visibility.SHOW);
                        view.setProgressBar(Visibility.HIDE);

                    } // TODO errore somtg went wrong da afggiungere
                    // errorr comunicazione, no internet
                    // mostra error e nascondi progression
                    view.setErrorPage(Visibility.SHOW);
                    view.setProgressBar(Visibility.HIDE);
                }
            });
    }
}
