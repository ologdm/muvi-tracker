package com.example.muvitracker.mainactivity.mylist;
import java.util.List;


public class MylistPresenter implements MylistContract.Presenter {


    // 1 ATTRIBUTI
    private MylistRepository mylistRepository = MylistRepository.getInstance();
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


    // 3.2 passo lista aggiornata: adapter->reository
    // serve per Chechbox, setWatched
    @Override
    public void setList(List<MylistDto> prefList) {
        mylistRepository.setPrefList(prefList);
    }


}

