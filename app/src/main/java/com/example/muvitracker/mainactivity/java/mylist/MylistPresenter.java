package com.example.muvitracker.mainactivity.java.mylist;

import com.example.muvitracker.repo.java.MylistRepo;
import com.example.muvitracker.repo.java.dto.DetailsDto;

import java.util.List;


public class MylistPresenter implements MylistContract.Presenter {


    // 1 ATTRIBUTI
    private MylistRepo mylistRepository = MylistRepo.getInstance();
    private MylistContract.View view;


    // 2. COSTRUTTORE
    public MylistPresenter(MylistContract.View view) {
        this.view = view;
    }


    // 3. CONTRACT

    // 3.1 carica dati: repository->adapter
    @Override
    public void loadData() {
        view.updateUi(mylistRepository.getPrefList());
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
        mylistRepository.toggleFavouriteItem(dto);
        // passo il click, che innescherà il cambio stato
        System.out.println("test toggleLiked");
        loadData();
    }

    // la uso per checkbox - > passo lista aggiornata
    @Override
    public void setList(List<DetailsDto> prefList) {
        mylistRepository.setPrefList(prefList);
        loadData();
    }


}

