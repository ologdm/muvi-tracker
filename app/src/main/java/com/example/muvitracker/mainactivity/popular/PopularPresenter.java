package com.example.muvitracker.mainactivity.popular;

import com.example.muvitracker.utils.UiUtils;
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
// 2. Messaggio personalizzato su ErrorPage.textview -> (no internet + "something went wrong")




public class PopularPresenter implements PopularContract.Presenter {



    // 1. ATTRIBUTI
    private PopularContract.View view;
    private PopularRepo repositoryPopular = PopularRepo.getIstance();



    // 2. COSTRUTTORE
    public PopularPresenter(PopularContract.View v) {
        this.view = v;
    }



    // 3. CONTRACT METHODS

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


        repositoryPopular.callPopular(
            new MyRetrofitListCallback<MovieDto>() {


            @Override
            public void onSuccess(List<MovieDto> list) {
                view.updateUi(list);

                // EmptyStates 2 - successo
                view.setProgressBar(Visibility.HIDE);
                // ??? corretto mettere null?
                view.setErrorPage(Visibility.HIDE, null);
            }


            @Override
            public void onError(Throwable throwable) {
                // Mostra errori su logcat sempre
                throwable.printStackTrace();

                // EmptyStates 3 - sempre
                // errore -> nascondo Rv
                view.setRvVisibility(Visibility.HIDE);

                if (throwable instanceof IOException) {
                    // EmptyStates 3 - caso noInternet
                    view.setErrorPage(Visibility.SHOW, UiUtils.NO_INTERNET);
                    view.setProgressBar(Visibility.HIDE);

                } else {
                    // EmptyStates 3 - altro errore
                    view.setErrorPage(Visibility.SHOW, UiUtils.OTHER_ERROR_MESSAGE);
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
