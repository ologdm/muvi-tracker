package com.example.muvitracker.mainactivity.boxofficefragment;

import com.example.muvitracker.repository.dto.BoxofficeDto;

import java.util.List;

public interface BoxofficeContract {

    public interface View {

        public void UpdateUi (List<BoxofficeDto> boxofficeList);



    }

    public interface Presenter {

        public void serverCallAndUpdateUi(List<BoxofficeDto> boxofficeList);

    }


}
