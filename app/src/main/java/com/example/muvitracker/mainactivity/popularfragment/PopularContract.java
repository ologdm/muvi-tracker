package com.example.muvitracker.mainactivity.popularfragment;

import androidx.annotation.NonNull;

import com.example.muvitracker.utils.Visibility;
import com.example.muvitracker.repository.dto.MovieDto;

import java.util.List;

public interface PopularContract {


    public interface View {

        // 2°STEP
        @NonNull
        public void updateUi (List<MovieDto> list);

        @NonNull
        public void setProgressBar(Visibility visibility);

        @NonNull
        public void setErrorPage (Visibility visibility);

        public void setRvVisibility(Visibility visibility);


        // TODO 3°STEP - DETAILS
        public void startDetailsFragment (int movieId);

    }



    public interface Presenter {

        // 2°STEP
        @NonNull
        public void serverCallAndUpdateUi(boolean forceRefresh);


        // TODO 3°STEP - DETAILS
        public void onVHolderClick(int movieId);

    }
}
