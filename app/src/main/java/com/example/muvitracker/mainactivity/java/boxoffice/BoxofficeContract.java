package com.example.muvitracker.mainactivity.java.boxoffice;

import com.example.muvitracker.repo.java.dto.BoxofficeDto;
import com.example.muvitracker.utils.java.Visibility;

import java.util.List;


// == Popular

public interface BoxofficeContract {

    public interface View {

        //  2°STEP - POPULAR + BOXOFFICE + EMPTY STATES

        public void updateUi(List<BoxofficeDto> list); // aggiorna lista adapter

        public void setProgressBar(Visibility visibility);

        public void setErrorPage(Visibility visibility, String errorMsg);

        public void setRvVisibility(Visibility visibility); // -> casi specifici dove serve nascondere Rv se non viene caricata


        // 3°STEP - DETAILS
        public void startDetailsFragment(int movieId);

    }


    public interface Presenter {

        //  2°STEP
        public void serverCallAndUpdateUi(boolean forceRefresh); // passa dati a UpdateUi() da Repository()


        // 3°STEP

        public void onVHolderClick(int movieId);
    }

}
