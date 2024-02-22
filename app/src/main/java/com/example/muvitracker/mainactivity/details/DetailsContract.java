package com.example.muvitracker.mainactivity.details;

import com.example.muvitracker.repository.dto.DetailsDto;

public interface DetailsContract {

    public interface View {

        // 3°STEP - DETAILS

        //public void updateDetailsDto (DetailsDto detailsDto); // ??? Si puè eliminare?

        public void updateUi (DetailsDto detailsDto);

    }


    public interface Presenter {

        // 3°STEP
        // eliminato
        //public void passIdToCall(int movieId);
        // Modificato in private
        // public void getDataFromCall(); -> private callServer



        // TODO 5°STEP MYLIST
        //  1. add element
        //  2. remove element
        //  3. check If element is present in list
        //  4. cambiare funzioni getMovie(id)

        // 1, 2
        public void addItem (); // prendi da un repo e passa all' altro repo
        public void removeItem (); // rimuovi da myList repo
        // 3
        //public boolean checkId (int TraktId); // ciclo for di id con tutti gli elementi della lista

        // 4. Cambiare funzioni
        public void getMovie (int id);
        // 4.1 serverCall()
        // 4.2 getDataFromMylist (id)



    }



}
