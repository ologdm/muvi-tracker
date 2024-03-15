package com.example.muvitracker.mainactivity.popular.java;

import androidx.annotation.NonNull;

import com.example.muvitracker.utils.java.Visibility;
import com.example.muvitracker.repo.dto.PopularDto;

import java.util.List;

public interface PopularContract {



    public interface View {

        //  2°STEP - POPULAR + BOXOFFICE + EMPTY STATES

        @NonNull
        public void updateUi (List<PopularDto> list);

        @NonNull
        public void setProgressBar(Visibility visibility);

        @NonNull
        public void setErrorPage (Visibility visibility, String errorMsg);

        public void setRvVisibility(Visibility visibility);



        // 3°STEP - DETAILS
        public void startDetailsFragment (int movieId);

    }



    public interface Presenter {

        // 2°STEP
        @NonNull
        public void serverCallAndUpdateUi(boolean forceRefresh);


        // 3°STEP
        public void onVHolderClick(int movieId);

    }
}
