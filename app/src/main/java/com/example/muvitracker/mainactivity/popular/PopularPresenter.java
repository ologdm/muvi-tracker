package com.example.muvitracker.mainactivity.popular;

import com.example.muvitracker.utils.Visibility;
import com.example.muvitracker.utils.MyRetrofitListCallback;
import com.example.muvitracker.repository.PopularRepo;
import com.example.muvitracker.repository.dto.MovieDto;

import java.io.IOException;
import java.util.List;

//          2° STEP->
// 1. metodo ServerCall+UpdateData()
// 2. Empty states logic:
//      1) prima di risposta server
//      2) succceso
//      3) error -> caso noInternet, altri casi

// 3. Force Refresh - > boolean per SwipeToRefresh
//    (boolean forceRefresh):
//          - true - comportamento swipeRefresh
//          - false - default


//          3°STEP
// 1. metodo VHCLick


// TODO
//  1 ->  Altro tipi errore -> add "something went wrong" a textview in metodo ServerCall+UpdateData()


public class PopularPresenter implements PopularContract.Presenter {


    // 1 ATTRIBUTI
    PopularContract.View view;
    PopularRepo repositoryPopular = PopularRepo.getIstance();


    // 2 COSTRUTTORE
    public PopularPresenter(PopularContract.View v) {
        this.view = v;
    }


    // 3 CONTRACT METHODS

    //     2°STEP

    // 3.1
    // - Repository->Adapter
    // - EmptyStates Management
    @Override
    public void serverCallAndUpdateUi(boolean forceRefresh) {

        // EmptyStates 1 - prima di risposta server
        // error page (default -> .GONE)
        // !! else obbligatorio, altrimenti le fa entrambe
        if (forceRefresh) {
            view.setProgressBar(Visibility.HIDE);
        } else {
            view.setProgressBar(Visibility.SHOW);
        }


        repositoryPopular.callPopular(new MyRetrofitListCallback<MovieDto>() {


            @Override
            public void onSuccess(List<MovieDto> list) {
                view.updateUi(list);

                // EmptyStates 2 - successo
                view.setProgressBar(Visibility.HIDE);
                view.setErrorPage(Visibility.HIDE);
            }


            @Override
            public void onError(Throwable throwable) {
                // Mostra errori su logcat sempre
                throwable.printStackTrace();

                // EmptyStates 3 - sempre
                // nascondo sempre la RV gia caricata in caso di errore
                view.setRvVisibility(Visibility.HIDE);


                if (throwable instanceof IOException) {
                    // EmptyStates 3 - caso noInternet
                    view.setErrorPage(Visibility.SHOW);
                    view.setProgressBar(Visibility.HIDE);

                } else {
                    // TODO
                    //  altro tipi errore -> add "something went wrong" a textview
                    view.setErrorPage(Visibility.SHOW);
                    view.setProgressBar(Visibility.HIDE);
                }
            }
        });
    }



    //          3°STEP
    // 3.2  CLick VH-> Create Details
    @Override
    public void onVHolderClick(int movieId) {
        view.startDetailsFragment(movieId);
    }


}
