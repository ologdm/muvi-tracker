package com.example.muvitracker.mainactivity.boxoffice;

import com.example.muvitracker.repository.BoxofficeRepo;
import com.example.muvitracker.repository.dto.BoxofficeDto;
import com.example.muvitracker.utils.MyRetrofitListCallback;
import com.example.muvitracker.utils.UiUtils;
import com.example.muvitracker.utils.Visibility;

import java.io.IOException;
import java.util.List;

//          2° STEP->
// = Popular


//          3°STEP
// = Popular


public class BoxofficePresenter implements BoxofficeContract.Presenter {


    // 1 ATTRIBUTI
    private BoxofficeContract.View view;
    private BoxofficeRepo repository = BoxofficeRepo.getIstance();

    // 2 COSTRUTTORE
    public BoxofficePresenter(BoxofficeContract.View v) {
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
        if (forceRefresh) {
            view.setProgressBar(Visibility.HIDE);
        } else {
            view.setProgressBar(Visibility.SHOW);
        }

        repository.callBoxoffice(new MyRetrofitListCallback<BoxofficeDto>() {
            @Override
            public void onSuccess(List<BoxofficeDto> list) {
                view.updateUi(list);

                // EmptyStates 2 - successo
                view.setProgressBar(Visibility.HIDE);
                view.setErrorPage(Visibility.HIDE, null);
                view.setRvVisibility(Visibility.SHOW);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();

                view.setRvVisibility(Visibility.HIDE);

                if (throwable instanceof IOException) {
                    view.setErrorPage(Visibility.SHOW, UiUtils.NO_INTERNET);
                    view.setProgressBar(Visibility.HIDE);

                } else {
                    view.setErrorPage(Visibility.SHOW, UiUtils.OTHER_ERROR_MESSAGE);
                    view.setProgressBar(Visibility.HIDE);

                }
            }
        });

    }


    @Override
    public void onVHolderClick(int movieId) {
        view.startDetailsFragment(movieId);
    }
}
