package com.example.muvitracker.mainactivity.details;

import com.example.muvitracker.mainactivity.mylist.MylistDto;
import com.example.muvitracker.mainactivity.mylist.MylistRepository;
import com.example.muvitracker.repository.DetailsRepo;
import com.example.muvitracker.repository.dto.DetailsDto;
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

// 3. setWatched/ isWatched; stessa cosa su repository


// TODO
//  1- EmptyStates - come su Popular
//  2 - - salva elementi su repository
//  4. METODI INTERNI: -> spostare su repository con cache


public class DetailsPresenter implements DetailsContract.Presenter {

    // 1. ATTRIBUTI
    // 1.1
    private final DetailsContract.View view;
    private final DetailsRepo detailsRepository = DetailsRepo.getIstance();

    // 5°STEP - pref list
    private final MylistRepository prefsRepository = MylistRepository.getInstance();

    // -> serve per salvare i dati scaricati dal server,
    //    per poter fare addItem
    DetailsDto detailsDto = new DetailsDto();


    // 2. COSTRUTTORE
    DetailsPresenter(DetailsContract.View view) {
        this.view = view;
    }


    // 3. CONTRACT

    // 3.1 add to repo
    @Override
    public void addItem() {
        prefsRepository.addToPref(detailsDto);
    }


    // 3.2 remove prof repo
    @Override
    public void removeItem(int traktId) {
        prefsRepository.removeFromPreflist(traktId);
    }


    // 3.3
    @Override
    public void getMovie(int traktId) {
        // check se
        boolean isIdIntoPref = checkMovieId(traktId);

        if (isIdIntoPref) {
            getDetailsFromPrefs(traktId);
        } else {
            getDetailsFromServer(traktId);
        }
    }


    // 3.4 funzione check - per FavoriteButton
    @Override
    public boolean checkMovieId(int traktId) {
        int inputId = traktId;

        List<MylistDto> prefList = prefsRepository.getPrefList();
        int prefListSize = prefsRepository.getPrefList().size();

        for (int i = 0; i < prefListSize; i++) {
            MylistDto myListDto = prefList.get(i);
            int idDaControllare = myListDto.getDetailsDto().getIds().getTrakt();
            if (inputId == idDaControllare) {
                return true;
            }
        }
        return false;
    }


    //  METODI INTERNI: ->  todo spostare su repository
    // 1 fromServer
    // 2.fromPrefs

    // 1. server
    private void getDetailsFromServer(int traktId) {

        // 1. passa id a call
        detailsRepository.setTraktMovieId(traktId);

        // 2. chiamata al server
        detailsRepository.callDetailsApi(
            new MyRetrofitCallback<DetailsDto>() {
                @Override
                public void onSuccess(DetailsDto obj) {
                    // 3.1 - aggiorno dto e views
                    // !!! devo chiamare quando ho i dati nella Callback
                    view.updateUi(obj);
                    detailsDto = obj;

                }

                @Override
                public void onError(Throwable throwable) {
                    // 3.2 - stampo errore su logcat (sia server 4xx,5xx, sia altro)
                    throwable.printStackTrace();
                }
            });
    }


    // prefs
    private void getDetailsFromPrefs (int traktId){
        MylistDto prefDto = prefsRepository.getMovie(traktId);

        view.updateUi(prefDto.getDetailsDto());
    }




    // WATCHED
    @Override
    public void setWatchedStatus(boolean status) {



        // 1 set
        prefsRepository.setMovie(prefDto);
        // 2 update quello che ho settato
        view.updateUi(prefDto.getDetailsDto());
    }




}





// 2. eliminare
    /*
    //    ritorno valore solo se id corrisponde
    public DetailsDto getCheckedMovie(int traktId) {
        int inputId = traktId;

        List<MylistDto> prefList = mylistRepository.getPrefList();
        int prefListSize = mylistRepository.getPrefList().size();

        for (int i = 0; i < prefListSize; i++) {
            MylistDto dto = prefList.get(i);
            int idDaControllare = dto.getDetailsDto().getIds().getTrakt();
            if (inputId == idDaControllare) {
                return dto.getDetailsDto(); // ritorna details
            }
        }
        return null; // ritorno null se non trovo niente
    }

     */
