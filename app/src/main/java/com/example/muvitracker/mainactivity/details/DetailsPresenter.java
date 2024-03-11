package com.example.muvitracker.mainactivity.details;

import com.example.muvitracker.mainactivity.mylist.PrefRepo;
import com.example.muvitracker.repo.DetailsRepo;
import com.example.muvitracker.repo.dto.DetailsDto;
import com.example.muvitracker.utils.MyRetrofitCallback;

import java.util.List;

//          3°STEP-> inizio
// 1. Metodo - passa Id a repository
// 2. Metodo - passa Dto a Fragment (in callback)


// 5°STEP
// 1. funzione checkId
// 2. funzione generica getMovie
//          - private callServer
//          - private getFromPrefs

// 3. setWatched/ isWatched;
//      - dto unificato details
//      - caso1 solo watched checkbox: salva solo in presenter,
//      - caso2 watched checkbox + addItem : salva dal presenter in repository



// TODO
//  1- EmptyStates - come su Popular
//  2 - - salva elementi su repository
//  4. METODI INTERNI: -> spostare su repository con cache


public class DetailsPresenter implements DetailsContract.Presenter {

    // 1. ATTRIBUTI
    // 1.1
    private final DetailsContract.View view;
    private final DetailsRepo detailsRepo = DetailsRepo.getIstance();

    // 5°STEP - pref list
    private final PrefRepo prefsRepo = PrefRepo.getInstance();

    DetailsDto presenterDto = new DetailsDto();

    boolean checkId;


    // 2. COSTRUTTORE
    DetailsPresenter(DetailsContract.View view) {
        this.view = view;
    }


    private boolean checkMovieId(int traktId) {
        int inputId = traktId;

        // pref list
        List<DetailsDto> prefList = prefsRepo.getPrefList();
        int prefListSize = prefsRepo.getPrefList().size();

        for (int i = 0; i < prefListSize; i++) {
            DetailsDto prefDto = prefList.get(i);
            int idDaControllare = prefDto.getIds().getTrakt();
            if (inputId == idDaControllare) {
                return true;
            }
        }
        return false;
    }


    public void load() {
        // TODO loading
        // repository.getData(id) {
        //    // success, hide loading, show data

        //    // error, hide loading, show error
        // }
    }



    // 3.3
    // TODO fare una callback unica success error sia per server che cache
    @Override
    public void getMovie(int traktId) {

        boolean isIdIntoPref = checkMovieId(traktId);

        // TODO spostare in repository 6°STEP
        if (isIdIntoPref) {
            getItemFromPrefs(traktId);
        } else {
            getItemFromServer(traktId);
        }
    }


    // 3.4 funzione check
    // - per favorite button








    //  METODI INTERNI: ->  todo spostare su repository
    // 1 fromServer
    // 2.fromPrefs

    // 1. server
    private void getItemFromServer(int traktId) {
        // 2. chiamata al server
        detailsRepo.callDetailsApi(
            traktId,
            new MyRetrofitCallback<DetailsDto>() {
                @Override
                public void onSuccess(DetailsDto dto) {
                    // repo -> presenter
                    presenterDto = dto;
                    // presenter -> fragment
                    view.updateUi(presenterDto);
                }

                @Override
                public void onError(Throwable throwable) {
                    // 3.2 - stampo errore su logcat (sia server 4xx,5xx, sia altro)
                    throwable.printStackTrace();
                }
            });
    }


    // prefs
    private void getItemFromPrefs(int traktId){
        // repo -> presenter
        presenterDto = prefsRepo.getMovie(traktId);
        // presenter -> fragment
        view.updateUi(presenterDto);
        //view.
    }




    // (eugi)
    // gestisce cambio stato del liked, e dice alla ui di aggiornarsi con il nuovo stato
    // delego la logica del cambio stata a repository
    @Override
    public void toggleFavourite() {
        // 1 prendo stato aggiornato da repo
        // !! perche repo gestisce il cambio stato
        boolean isLiked = prefsRepo.toggleFavouriteItem(presenterDto);
        // 2 aggiorno dto locale
        presenterDto.setLiked(isLiked);
        // 3 aggiorno view da dto locale
        view.updateFavoriteIcon(isLiked);
    }


    // WATCHED (dima)
    // tutte le azioni
    @Override
    public void updateWatched(boolean isWatched) {
        // update presenter dto
        presenterDto.setWatched(isWatched);
        // update repo element
        prefsRepo.updateWatchedItem(presenterDto);

    }


}


