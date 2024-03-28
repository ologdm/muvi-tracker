package com.example.muvitracker.injava.mainactivity.details;


import com.example.muvitracker.injava.repo.dto.DetailsDto;

public interface DetailsContract {

    // 3°STEP - Details
    public interface View {

        public void updateUi (DetailsDto detailsDto);
        public void updateFavoriteIcon(boolean isFavourite); // eugi
        public void updateWatchedCheckbox(boolean isWatched); // dima

    }


    // 3°STEP - Details - eliminato funzioni
    // 5°STEP - MyList - rifatto tutte le funzioni
    public interface Presenter {

        public void getMovie (int traktId); // su repo

        // check se elemento nella lista
        //public boolean checkMovieId(int traktId);


        //public void setWatchedStatus (MylistDto prefDto); // set poi aggiorna dati

        public void updateWatched(boolean status);
        public void toggleFavourite();


    }



}
