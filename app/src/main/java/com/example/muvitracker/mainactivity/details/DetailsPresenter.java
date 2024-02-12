package com.example.muvitracker.mainactivity.details;

import com.example.muvitracker.repository.DetailsRepo;
import com.example.muvitracker.repository.dto.DetailsDto;
import com.example.muvitracker.utils.MyRetrofitCallback;

//          3°STEP-> inizio
// 1. Metodo - passa Id a repository
// 2. Metodo - passa Dto a Fragment (in callback)

// TODO
//  1- EmptyStates - come su Popular


public class DetailsPresenter implements DetailsContract.Presenter {

    // 1. ATTRIBUTI
    // 1.1
    private final DetailsContract.View view;
    private final DetailsRepo repository = DetailsRepo.getIstance();

    // 2. COSTRUTTORE
    DetailsPresenter(DetailsContract.View view) {
        this.view = view;
    }


    // 3. CONTRACT METHODS
    // 3.1 Fragment -> Repository (passo Id)
    @Override
    public void passIdToCall(int movieId) {
        repository.setMovieId(movieId);
    }


    // 3.2 Repository -> Fragm (passo Dto)
    @Override
    public void getDataFromCall() {

        // TODO 1

        repository.callDetailsApi(new MyRetrofitCallback<DetailsDto>() {
            @Override
            public void onSuccess(DetailsDto obj) {

                // !!! entrambi i metodi devo chiamarli quando ho i dati, cioe nella Callback
                // Aggiorno Dto
                view.updateDetailsDto(obj);
                // Aggiorno Views
                view.updateUi();

            }

            @Override
            public void onError(Throwable throwable) {
                // Stampo Errore su Logcat
                throwable.printStackTrace();
            }
        });


    }


}
