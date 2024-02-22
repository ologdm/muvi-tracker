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

// TODO
//  1- EmptyStates - come su Popular
//  2 - 5°STEP - salva elementi su repository


public class DetailsPresenter implements DetailsContract.Presenter {

    // 1. ATTRIBUTI
    // 1.1
    private final DetailsContract.View view;
    private final DetailsRepo detailsRepository = DetailsRepo.getIstance();


    //     TODO 5°STEP MYLIST
    private final MylistRepository mylistRepository = MylistRepository.getInstance();


    //     TODO 5°STEP MYLIST
    DetailsDto detailsDto = new DetailsDto();


    // 2. COSTRUTTORE
    DetailsPresenter(DetailsContract.View view) {
        this.view = view;
    }


    // 3. CONTRACT METHODS


    // Click icona Add:
    @Override
    public void addItem() {
        mylistRepository.addToPref(detailsDto);

    }


    // Click icona aggiungi:
    @Override
    public void removeItem() {

    }





    // METODI INTERNI:
    // 1.server
    // 2.mylist
    // 3.check id

    // 1. Server
    private void updateDetailsFromServer(int traktId) {
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

    /*
    // 2. Mylist - non serve
    private void updateDetailsFromMylist(int traktId) {
        // ottieni elemento da
        mylistRepository.getMyPrefList().get(traktId);
    }

     */


    // 3. Id check OK
    //    ritorno valore solo se id corrisponde
    public DetailsDto getMovieFromMylist(int id) {
        int inputId = id;

        List<MylistDto> mylist = mylistRepository.getPrefList();
        int mylistSize = mylistRepository.getPrefList().size();

        for (int i = 0; i < mylistSize; i++) {
            MylistDto myListDto = mylist.get(i);
            int idDaControllare = myListDto.getDetailsDto().getIds().getTrakt();
            if (inputId == idDaControllare) {
                return myListDto.getDetailsDto(); // ritorna details
            }
        }
        return null; // ritorno null se non trovo niente
    }


    // METODO SCEGLI OK
    @Override
    public void getMovie ( int id){
        if (getMovieFromMylist(id)==null) {
            updateDetailsFromServer(id);
        } else {
            DetailsDto movieFromMylist = getMovieFromMylist(id);
            view.updateUi(movieFromMylist);
        }
    }




    }



