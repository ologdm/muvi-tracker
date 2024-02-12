package com.example.muvitracker.mainactivity.boxofficefragment;

import com.example.muvitracker.repository.BoxofficeRepository;
import com.example.muvitracker.repository.dto.BoxofficeDto;
import com.example.muvitracker.utils.MyRetrofitListCallback;
import com.example.muvitracker.utils.Visibility;

import java.util.List;

public class BoxofficePresenter implements BoxofficeContract.Presenter {

    private BoxofficeContract.View view;
    private BoxofficeRepository repository = BoxofficeRepository.getIstance();


    public BoxofficePresenter(BoxofficeContract.View v) {
        this.view = v;
    }



    @Override
    public void serverCallAndUpdateUi(boolean forceRefresh) {
        // di base
        // forseRefresh true, false
        if (forceRefresh) {
            view.setProgressBar(Visibility.HIDE);
        } else {
            view.setProgressBar(Visibility.SHOW);
        }


        repository.callBoxoffice(new MyRetrofitListCallback<BoxofficeDto>() {
            @Override
            public void onSuccess(List<BoxofficeDto> list) {
                view.updateUi(list);
                System.out.println("test");
                view.setProgressBar(Visibility.HIDE);
                view.setErrorPage(Visibility.HIDE);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace(); // da mettere sempre

                view.setProgressBar(Visibility.HIDE);
                view.setErrorPage(Visibility.SHOW);
            }
        });

    }
}
