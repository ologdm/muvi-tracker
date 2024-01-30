package com.example.muvitracker.mainactivity.popularfragment;

import androidx.annotation.NonNull;

import com.example.muvitracker.mainactivity.VisibilityEnum;
import com.example.muvitracker.repository.dto.MovieDto;

import java.util.List;

public interface PopularContract {

    public interface View {

        @NonNull
        public void updateUi (List<MovieDto> list);

        @NonNull
        public void setProgressBar(VisibilityEnum visibility);

        @NonNull
        public void setErrorPage (VisibilityEnum visibility);


    }


    public interface Presenter {

        // 1 metodo
        @NonNull
        public void serverCallAndUpdateUi();

    }
}
