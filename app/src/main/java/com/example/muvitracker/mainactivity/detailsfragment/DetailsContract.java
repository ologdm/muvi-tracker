package com.example.muvitracker.mainactivity.detailsfragment;

import com.example.muvitracker.repository.dto.DetailsDto;

import retrofit2.http.PUT;

public interface DetailsContract {

    public interface View {

        public void updateDetailsDto (DetailsDto detailsDto);

        public void updateUi ();

    }


    public interface Presenter {

        // OK
        public void passIdToCall(int movieId);


        public void getDataFromCall ();





    }



}
