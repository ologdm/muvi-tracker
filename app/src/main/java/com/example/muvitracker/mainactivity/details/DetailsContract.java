package com.example.muvitracker.mainactivity.details;

import com.example.muvitracker.repository.dto.DetailsDto;

public interface DetailsContract {

    public interface View {

        // 3°STEP - DETAILS

        public void updateDetailsDto (DetailsDto detailsDto); // ??? Si puè eliminare?

        public void updateUi ();

    }


    public interface Presenter {

        // 3°STEP

        public void passIdToCall(int movieId);

        public void getDataFromCall ();





    }



}
