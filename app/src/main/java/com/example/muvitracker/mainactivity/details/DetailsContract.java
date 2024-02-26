package com.example.muvitracker.mainactivity.details;

import com.example.muvitracker.mainactivity.mylist.MylistDto;
import com.example.muvitracker.repository.dto.DetailsDto;

public interface DetailsContract {

    // 3°STEP - Details
    public interface View {

        public void updateUi (DetailsDto detailsDto);

    }


    // 3°STEP - Details - eliminato funzioni
    // 5°STEP - MyList - rifatto tutte le funzioni
    public interface Presenter {

        public void addItem();

        public void removeItem(int traktId);

        public void getMovie (int traktId);

        // checkl se elemento nella lista
        public boolean checkMovieId(int traktId);


        //public void setWatchedStatus (MylistDto prefDto); // set poi aggiorna dati

        public void setWatchedStatus (boolean status);


    }



}
