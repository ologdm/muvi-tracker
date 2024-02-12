package com.example.muvitracker.mainactivity.detailsfragment;

import com.example.muvitracker.repository.DetailsRepository;
import com.example.muvitracker.repository.dto.DetailsDto;
import com.example.muvitracker.utils.MyRetrofitCallback;

public class DetailsPresenter implements DetailsContract.Presenter {

    private DetailsContract.View view;

    private DetailsRepository repository = DetailsRepository.getIstance();

    // OK
    DetailsPresenter(DetailsContract.View view) {
        this.view = view;
    }


    // OK
    // 1. fragm -> repos
    @Override
    public void passIdToCall(int movieId) {
        repository.setMovieId(movieId);
    }


    // 2. repos -> frag
    @Override
    public void getDataFromCall() {

        repository.callDetails(new MyRetrofitCallback<DetailsDto>() {
            @Override
            public void onSuccess(DetailsDto obj) {
                view.updateDetailsDto(obj);
                view.updateUi();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        // TODO fare Error+Loading

    }


}
