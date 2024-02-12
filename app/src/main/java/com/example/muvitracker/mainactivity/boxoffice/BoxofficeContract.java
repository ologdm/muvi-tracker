package com.example.muvitracker.mainactivity.boxoffice;

import com.example.muvitracker.repository.dto.BoxofficeDto;
import com.example.muvitracker.utils.Visibility;

import java.util.List;


// == Popular

public interface BoxofficeContract {

    public interface View {

        public void updateUi(List<BoxofficeDto> list); // aggiorna lista adapter

        public void setProgressBar (Visibility visibility);
        public void setErrorPage (Visibility visibility);
        public void setRvVisibility (Visibility visibility); // -> casi specifici dove serve nascondere Rv se non viene caricata

    }


    public interface Presenter {

        public void serverCallAndUpdateUi(boolean forceRefresh); // passa dati a UpdateUi() da Repository()

    }


}
