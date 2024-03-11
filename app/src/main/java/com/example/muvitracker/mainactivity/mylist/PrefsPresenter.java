package com.example.muvitracker.mainactivity.mylist;

import com.example.muvitracker.repo.dto.DetailsDto;

import java.util.List;


public class PrefsPresenter implements PrefsContract.Presenter {


    // 1 ATTRIBUTI
    private PrefRepo prefRepository = PrefRepo.getInstance();
    private PrefsContract.View view;


    // 2. COSTRUTTORE
    public PrefsPresenter(PrefsContract.View view) {
        this.view = view;
    }


    // 3. CONTRACT

    // 3.1 carica dati: repository->adapter
    @Override
    public void loadData() {
        view.updateUi(prefRepository.getPrefList());
    }


    // 3.2 passo lista aggiornata: adapter -> repository
    // serve per Chechbox, setWatched
    /*
    @Override
    public void setList(List<MylistDto> prefList) {
        prefRepository.setPrefList(prefList);
    }
     */


    // aggiorno
    @Override
    public void toggleFavorite(DetailsDto dto) {
        prefRepository.toggleFavouriteItem(dto);
        // passo il click, che innescherà il cambio stato
        System.out.println("test toggleLiked");
        loadData();
    }

    // la uso per checkbox - > passo lista aggiornata
    @Override
    public void setList(List<DetailsDto> prefList) {
        prefRepository.setPrefList(prefList);
        loadData();
    }


}

